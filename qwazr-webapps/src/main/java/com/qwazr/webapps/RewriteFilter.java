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
package com.qwazr.webapps;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RewriteFilter implements Filter {

	public static final String PARAM_PATTERN = "com.qwazr.webapps.rewrite.pattern";
	public static final String PARAM_REPLACE = "com.qwazr.webapps.rewrite.replace";

	private volatile Pattern pattern;
	private volatile String replace;

	@Override
	public void init(final FilterConfig filterConfig) throws ServletException {
		pattern = Pattern.compile(filterConfig.getInitParameter(PARAM_PATTERN));
		replace = filterConfig.getInitParameter(PARAM_REPLACE);
	}

	@Override
	final public void doFilter(final ServletRequest req, final ServletResponse rep, final FilterChain chain)
			throws IOException, ServletException {
		final HttpServletRequest request = (HttpServletRequest) req;
		final Matcher matcher;
		final StringBuffer reqUrl = request.getRequestURL();
		synchronized (pattern) {
			matcher = pattern.matcher(reqUrl);
		}
		final String newUrl = matcher.replaceAll(replace);
		if (reqUrl.equals(newUrl))
			chain.doFilter(req, rep);
		else
			request.getRequestDispatcher(newUrl).forward(request, rep);
	}

	@Override
	public void destroy() {
	}
}
