package com.qwazr.crawler.web;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.qwazr.crawler.web.driver.BrowserDriver;
import com.qwazr.crawler.web.driver.BrowserDriverBuilder;
import com.qwazr.crawler.web.service.WebCrawlDefinition;
import com.qwazr.crawler.web.service.WebCrawlerServiceInterface;
import com.qwazr.library.AbstractLibrary;
import com.qwazr.utils.IOUtils;
import com.qwazr.utils.json.JsonMapper;

import java.io.IOException;
import java.net.URISyntaxException;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WebCrawlerTool extends AbstractLibrary {

	final public WebCrawlDefinition config = null;

	@Override
	public void load() {
	}

	/**
	 * Create a new web crawler client instance.
	 * This API queries the cluster to get the current active node for the WebCrawler service.
	 *
	 * @param local Set to true to target the local service
	 * @param group the targeted group
	 * @return a new WebCrawlerServiceInterface instance
	 * @throws URISyntaxException
	 */
	@JsonIgnore
	public WebCrawlerServiceInterface getClient(Boolean local, String group) throws URISyntaxException {
		return WebCrawlerServiceInterface.getClient(local, group);
	}

	@JsonIgnore
	public WebCrawlDefinition getNewWebCrawlDefinition() {
		return config == null ? new WebCrawlDefinition() : (WebCrawlDefinition) config.clone();
	}

	@JsonIgnore
	public BrowserDriver getNewWebDriver(final IOUtils.CloseableContext context, final String json)
			throws ReflectiveOperationException, SecurityException, IOException {
		WebCrawlDefinition webCrawlDef = JsonMapper.MAPPER.readValue(json, WebCrawlDefinition.class);
		return new BrowserDriverBuilder(webCrawlDef).build();
	}

	@JsonIgnore
	public BrowserDriver getNewWebDriver(final IOUtils.CloseableContext context) throws ReflectiveOperationException {
		return new BrowserDriverBuilder(config).build();
	}
}
