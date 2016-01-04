/**
 * Copyright 2014-2016 Emmanuel Keller / QWAZR
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
package com.qwazr.job.script;

import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.ws.rs.core.Response.Status;

import com.qwazr.utils.server.ServerException;

public class ScriptServiceImpl implements ScriptServiceInterface {

	@Override
	public ScriptRunStatus runScript(String scriptPath) {
		return runScriptVariables(scriptPath, null);
	}

	@Override
	public ScriptRunStatus runScriptVariables(String scriptPath,
			Map<String, String> variables) {
		try {
			return ScriptManager.INSTANCE.runAsync(scriptPath, variables);
		} catch (Exception e) {
			throw ServerException.getJsonException(e);
		}
	}

	private ScriptRunThread getRunThread(String run_id) throws ServerException {
		ScriptRunThread runThread = ScriptManager.INSTANCE.getRunThread(run_id);
		if (runThread == null)
			throw new ServerException(Status.NOT_FOUND, "No status found");
		return runThread;
	}

	@Override
	public ScriptRunStatus getRunStatus(String run_id) {
		try {
			return getRunThread(run_id).getStatus();
		} catch (ServerException e) {
			throw e.getTextException();
		}
	}

	@Override
	public String getRunOut(String run_id) {
		try {
			return getRunThread(run_id).getOut();
		} catch (ServerException e) {
			throw e.getTextException();
		}
	}

	@Override
	public String getRunErr(String run_id) {
		try {
			return getRunThread(run_id).getErr();
		} catch (ServerException e) {
			throw e.getTextException();
		}
	}

	@Override
	public Map<String, ScriptRunStatus> getRunsStatus(Boolean local,
			Integer msTimeout) {
		try {
			if (local != null && local) {
				Map<String, ScriptRunStatus> localRunStatusMap = ScriptManager.INSTANCE
						.getRunsStatus();
				if (localRunStatusMap == null)
					localRunStatusMap = Collections.emptyMap();
				return localRunStatusMap;
			}
			TreeMap<String, ScriptRunStatus> globalRunStatusMap = new TreeMap<String, ScriptRunStatus>();
			globalRunStatusMap.putAll(ScriptManager.INSTANCE.getNewClient(
					msTimeout).getRunsStatus(false, msTimeout));
			return globalRunStatusMap;
		} catch (URISyntaxException e) {
			throw ServerException.getJsonException(e);
		}
	}

	@Override
	public Set<String> getSemaphores(Boolean local, Integer msTimeout) {
		try {
			if (local != null && local) {
				Set<String> semaphores = new HashSet<String>();
				ScriptManager.INSTANCE.getSemaphores(semaphores);
				return semaphores;
			}
			return ScriptManager.INSTANCE.getNewClient(msTimeout)
					.getSemaphores(false, msTimeout);
		} catch (URISyntaxException e) {
			throw ServerException.getJsonException(e);
		}

	}

	@Override
	public Set<String> getSemaphoreOwners(String semaphore_id, Boolean local,
			Integer msTimeout) {
		try {
			if (local != null && local) {
				Set<String> owners = new HashSet<String>();
				ScriptManager.INSTANCE.getSemaphoreOwners(semaphore_id, owners);
				return owners;
			}
			return ScriptManager.INSTANCE.getNewClient(msTimeout)
					.getSemaphoreOwners(semaphore_id, false, msTimeout);
		} catch (URISyntaxException e) {
			throw ServerException.getJsonException(e);
		}
	}
}
