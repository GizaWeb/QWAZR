/**
 * Copyright 2015-2016 Emmanuel Keller / QWAZR
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.qwazr.graph;

import com.qwazr.cluster.manager.ClusterManager;
import com.qwazr.database.store.Table;
import com.qwazr.database.store.Tables;
import com.qwazr.graph.model.GraphDefinition;
import com.qwazr.utils.LockUtils;
import com.qwazr.utils.json.DirectoryJsonManager;
import com.qwazr.utils.server.RemoteService;
import com.qwazr.utils.server.ServerBuilder;
import com.qwazr.utils.server.ServerException;
import org.apache.commons.io.FileUtils;

import javax.ws.rs.core.Response.Status;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;

public class GraphManager extends DirectoryJsonManager<GraphDefinition> {

	public final static String SERVICE_NAME_GRAPH = "graph";

	private final LockUtils.ReadWriteLock rwl = new LockUtils.ReadWriteLock();

	static GraphManager INSTANCE = null;

	public File directory;

	private final Map<String, GraphInstance> graphMap;

	public synchronized static void load(final ServerBuilder serverBuilder) throws IOException {
		if (INSTANCE != null)
			throw new IOException("Already loaded");
		File graphDirectory = new File(serverBuilder.getServerConfiguration().dataDirectory, SERVICE_NAME_GRAPH);
		if (!graphDirectory.exists())
			graphDirectory.mkdir();
		INSTANCE = new GraphManager(serverBuilder.getExecutorService(), graphDirectory);
		for (String name : INSTANCE.nameSet())
			INSTANCE.addNewInstance(name, INSTANCE.get(name));
		serverBuilder.registerWebService(GraphServiceImpl.class);
	}

	public static GraphManager getInstance() {
		if (INSTANCE == null)
			throw new RuntimeException("The graph service is not enabled");
		return INSTANCE;
	}

	private GraphManager(ExecutorService executorService, File directory) throws ServerException, IOException {
		super(directory, GraphDefinition.class);
		this.directory = directory;
		graphMap = new HashMap<>();
	}

	private GraphInstance addNewInstance(String graphName, GraphDefinition graphDef)
			throws IOException, ServerException {
		File dbDirectory = new File(directory, graphName);
		if (!dbDirectory.exists())
			dbDirectory.mkdir();
		Table table = Tables.getInstance(dbDirectory, true);
		GraphInstance graphInstance = new GraphInstance(graphName, table, graphDef);
		graphInstance.checkFields();
		graphMap.put(graphName, graphInstance);
		return graphInstance;
	}

	public GraphInstance getGraphInstance(final String graphName) throws ServerException {
		return rwl.readEx(() -> {
			GraphInstance graphInstance = graphMap.get(graphName);
			if (graphInstance == null)
				throw new ServerException(Status.NOT_FOUND, "Graph instance not found");
			return graphInstance;
		});
	}

	@Override
	public Set<String> nameSet() {
		return super.nameSet();
	}

	@Override
	public GraphDefinition get(final String name) throws IOException {
		return super.get(name);
	}

	public void createUpdateGraph(final String graphName, final GraphDefinition graphDef)
			throws IOException, ServerException {
		rwl.writeEx(() -> {
			super.set(graphName, graphDef);
			graphMap.remove(graphName);
			addNewInstance(graphName, graphDef);
		});
	}

	@Override
	public GraphDefinition delete(String graphName) throws ServerException, IOException {
		return rwl.writeEx(() -> {
			GraphDefinition graphDef = super.delete(graphName);
			File dbDirectory = new File(directory, graphName);
			Table table = Tables.getInstance(dbDirectory, false);
			if (table != null) {
				table.close();
				table.delete();
			}
			FileUtils.deleteDirectory(dbDirectory);
			graphMap.remove(graphName);
			return graphDef;
		});
	}

	GraphMultiClient getMultiClient() throws URISyntaxException {
		return new GraphMultiClient(
				RemoteService.build(ClusterManager.INSTANCE.getNodesByGroupByService(null, SERVICE_NAME_GRAPH)));
	}
}
