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
package com.qwazr.webapps;

import com.qwazr.cluster.manager.ClusterManager;
import com.qwazr.utils.file.TrackedInterface;
import com.qwazr.utils.server.GenericServer;
import com.qwazr.utils.server.ServerBuilder;
import com.qwazr.utils.server.ServerConfiguration;

import java.io.File;

public class WebappServer {

	public static GenericServer start() throws Exception {
		final ServerBuilder builder = new ServerBuilder(new ServerConfiguration(System.getProperties()));
		final File currentTempDir = new File(builder.getServerConfiguration().dataDirectory, "tmp");
		currentTempDir.mkdir();
		final TrackedInterface etcTracker =
				TrackedInterface.build(builder.getServerConfiguration().etcDirectories, null);
		ClusterManager.load(builder, null, null);
		WebappManager.load(builder, etcTracker, currentTempDir);
		etcTracker.check();
		return builder.build().start(true);
	}

	public static void main(String[] args) throws Exception {
		start();
	}

}