/**
 * Copyright 2014-2016 Emmanuel Keller / QWAZR
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
package com.qwazr.graph.test;

import com.google.common.io.Files;
import com.qwazr.graph.GraphServer;
import org.junit.runner.Description;
import org.junit.runner.notification.RunListener;

import javax.servlet.ServletException;
import java.io.File;
import java.io.IOException;

public class TestServer {

	public static boolean serverStarted = false;

	private static final String BASE_URL = "http://localhost:9091";

	public static synchronized void startServer() throws Exception {
		if (serverStarted)
			return;
		final File dataDir = Files.createTempDir();
		System.setProperty("QWAZR_DATA", dataDir.getAbsolutePath());
		System.setProperty("LISTEN_ADDR", "localhost");
		System.setProperty("PUBLIC_ADDR", "localhost");
		GraphServer.main(new String[]{});
		serverStarted = true;
	}
}