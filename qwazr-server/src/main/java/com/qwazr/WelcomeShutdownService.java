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
 **/
package com.qwazr;

import com.qwazr.utils.server.ServiceName;
import com.qwazr.utils.server.WelcomeService;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;

@RolesAllowed("welcome")
@Path("/")
@ServiceName("welcome")
public class WelcomeShutdownService extends WelcomeService {

	@DELETE
	@Path("/shutdown")
	public void shutdown() {
		new ShutdownThread();
	}

	private static class ShutdownThread implements Runnable {

		private ShutdownThread() {
			new Thread(this).start();
		}

		@Override
		public void run() {
			try {
				Thread.sleep(5000);
				Qwazr.stop(null);
			} catch (InterruptedException e) {
				Qwazr.LOGGER.warn(e.getMessage(), e);
			}
		}
	}
}
