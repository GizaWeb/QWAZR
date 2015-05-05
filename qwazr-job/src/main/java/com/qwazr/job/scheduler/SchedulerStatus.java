/**   
 * License Agreement for QWAZR
 *
 * Copyright (C) 2014-2015 OpenSearchServer Inc.
 * 
 * http://www.qwazr.com
 * 
 * This file is part of QWAZR.
 *
 * QWAZR is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * QWAZR is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with QWAZR. 
 *  If not, see <http://www.gnu.org/licenses/>.
 **/
package com.qwazr.job.scheduler;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.qwazr.job.script.ScriptRunStatus;

@JsonInclude(Include.NON_EMPTY)
public class SchedulerStatus extends SchedulerDefinition {

	public ScriptRunStatus script_status;

	public SchedulerStatus() {
		script_status = null;
	}

	public SchedulerStatus(ScriptRunStatus script_status) {
		this.script_status = script_status;
	}

	public SchedulerStatus(SchedulerDefinition scheduler) {
		super(scheduler);
	}
}
