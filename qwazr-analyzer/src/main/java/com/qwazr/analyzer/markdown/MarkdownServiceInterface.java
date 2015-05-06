/**
 * Copyright 2014-2015 OpenSearchServer Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 **/
package com.qwazr.analyzer.markdown;

import java.io.InputStream;
import java.util.Map;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

@Path("/analyze/markdown")
public interface MarkdownServiceInterface {

	@GET
	@Path("/")
	Map<String, MarkdownProcessorDefinition> listProcessors();

	@GET
	@Path("/{processor_name}")
	Map<String, MarkdownProcessorDefinition> getProcessor();

	@PUT
	@Path("/{processor_name}")
	MarkdownProcessorDefinition setProcessor(
			@PathParam("processor_name") String processorName,
			MarkdownProcessorDefinition processorDefinition);

	@DELETE
	@Path("/{processor_name}")
	void deleteProcessor(String processorName);

	@POST
	@Path("/{processor_name}")
	String convert(@QueryParam("path") String path, InputStream content);
}
