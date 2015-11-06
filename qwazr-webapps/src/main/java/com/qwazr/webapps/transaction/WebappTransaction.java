/**
 * Copyright 2014-2015 Emmanuel Keller / QWAZR
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
 **/
package com.qwazr.webapps.transaction;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.qwazr.webapps.exception.WebappException;
import com.qwazr.webapps.transaction.body.HttpBodyInterface;

import javax.script.ScriptException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response.Status;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.PrivilegedActionException;

public class WebappTransaction {

	private final FilePath filePath;

	private final ApplicationContext context;
	private final WebappHttpRequest request;
	private final WebappResponse response;

	public WebappTransaction(HttpServletRequest request, HttpServletResponse response, HttpBodyInterface body)
			throws IOException, URISyntaxException {
		this.response = new WebappResponse(response);
		FilePath fp = new FilePath(request.getPathInfo(), false);
		// First we try to find a sub context
		ApplicationContext ctx = WebappManager.INSTANCE.findApplicationContext(fp, this);
		if (ctx == null) {
			// The we test the ROOT context
			fp = new FilePath(request.getPathInfo(), true);
			ctx = WebappManager.INSTANCE.findApplicationContext(fp, this);
			if (ctx == null)
				throw new FileNotFoundException("No application found");
		}
		this.filePath = fp;
		this.context = ctx;
		this.request = new WebappHttpRequestImpl(context, request, body);
		this.response.variable("request", this.request);
		this.response.variable("response", this.response);
		this.response.variable("session", this.request.getSession());
	}

	final ApplicationContext getContext() {
		return context;
	}

	final WebappHttpRequest getRequest() {
		return request;
	}

	final WebappResponse getResponse() {
		return response;
	}

	final FilePath getFilePath() {
		return filePath;
	}

	public void execute()
			throws IOException, URISyntaxException, ScriptException, PrivilegedActionException, InterruptedException,
			ReflectiveOperationException, ServletException {
		String pathInfo = request.getPathInfo();
		StaticManager staticManager = StaticManager.INSTANCE;
		File staticFile = staticManager.findStatic(context, pathInfo);
		if (staticFile != null) {
			staticManager.handle(response, staticFile);
			return;
		}
		ControllerManager controllerManager = ControllerManager.INSTANCE;
		File controllerFile = controllerManager.findController(context, pathInfo);
		if (controllerFile != null) {
			controllerManager.handle(this, controllerFile);
			return;
		}
		throw new WebappException(Status.NOT_FOUND);
	}
}
