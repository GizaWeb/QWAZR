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
package com.qwazr.search.query;

import com.qwazr.search.index.QueryContext;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.automaton.Operations;
import org.apache.lucene.util.automaton.RegExp;

import java.io.IOException;

public class RegexpQuery extends AbstractQuery {

	final public String field;
	final public String text;
	final public Integer flags;
	final public Integer max_determinized_states;

	public RegexpQuery() {
		field = null;
		text = null;
		flags = null;
		max_determinized_states = null;
	}

	public RegexpQuery(final String field, final String text, final Integer flags) {
		this.field = field;
		this.text = text;
		this.flags = flags;
		this.max_determinized_states = null;
	}

	public RegexpQuery(final String field, final String text, final Integer flags, final Integer max_determinized_states) {
		this.field = field;
		this.text = text;
		this.flags = flags;
		this.max_determinized_states = max_determinized_states;
	}

	@Override
	final public Query getQuery(final QueryContext queryContext) throws IOException {
		return new org.apache.lucene.search.RegexpQuery(new Term(field, text), flags == null ? RegExp.ALL : flags,
				max_determinized_states == null ? Operations.DEFAULT_MAX_DETERMINIZED_STATES : max_determinized_states);
	}
}
