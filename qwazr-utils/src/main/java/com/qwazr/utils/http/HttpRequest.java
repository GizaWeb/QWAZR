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
package com.qwazr.utils.http;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.*;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

public abstract class HttpRequest<T extends HttpRequestBase> {

	final public static Base<HttpHead> Head(final String uri) {
		return new Base(new HttpHead(uri));
	}

	final public static Base<HttpHead> Head(final URI uri) {
		return new Base(new HttpHead(uri));
	}

	final public static Base<HttpGet> Get(final String uri) {
		return new Base(new HttpGet(uri));
	}

	final public static Base<HttpGet> Get(final URI uri) {
		return new Base(new HttpGet(uri));
	}

	final public static Base<HttpDelete> Delete(final String uri) {
		return new Base(new HttpDelete(uri));
	}

	final public static Base<HttpDelete> Delete(final URI uri) {
		return new Base(new HttpDelete(uri));
	}

	final public static Base<HttpTrace> Trace(final String uri) {
		return new Base(new HttpTrace(uri));
	}

	final public static Base<HttpTrace> Trace(final URI uri) {
		return new Base(new HttpTrace(uri));
	}

	final public static Base<HttpOptions> Options(final String uri) {
		return new Base(new HttpOptions(uri));
	}

	final public static Base<HttpOptions> Options(final URI uri) {
		return new Base(new HttpOptions(uri));
	}

	final public static Entity<HttpPost> Post(final String uri) {
		return new Entity(new HttpPost(uri));
	}

	final public static Entity<HttpPost> Post(final URI uri) {
		return new Entity(new HttpPost(uri));
	}

	final public static Entity<HttpPut> Put(final String uri) {
		return new Entity(new HttpPut(uri));
	}

	final public static Entity<HttpPut> Put(final URI uri) {
		return new Entity(new HttpPut(uri));
	}

	final public static Entity<HttpPatch> Patch(final String uri) {
		return new Entity(new HttpPatch(uri));
	}

	final public static Entity<HttpPatch> Patch(final URI uri) {
		return new Entity(new HttpPatch(uri));
	}


	public final T request;

	private HttpRequest(T request) {
		this.request = request;
	}

	final public CloseableHttpResponse execute() throws IOException {
		return HttpClients.HTTP_CLIENT.execute(request);
	}

	final public CloseableHttpResponse execute(final HttpClientContext context) throws IOException {
		return HttpClients.HTTP_CLIENT.execute(request, context);
	}

	public final HttpRequest<T> addHeader(final Header header) {
		request.addHeader(header);
		return this;
	}

	public final HttpRequest<T> addHeader(final String name, final String value) {
		request.addHeader(name, value);
		return this;
	}

	public static class Base<B extends HttpRequestBase> extends HttpRequest<B> {

		private Base(B request) {
			super(request);
		}
	}

	public static class Entity<E extends HttpEntityEnclosingRequestBase> extends Base<E> {

		private Entity(E request) {
			super(request);
		}

		final public Entity<E> bodyString(final String content, final ContentType type) {
			request.setEntity(new StringEntity(content, type));
			return this;
		}

		final public Entity<E> bodyStream(final InputStream stream, ContentType type) {
			request.setEntity(new InputStreamEntity(stream, type));
			return this;
		}

	}

}
