package com.aerse.web;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.DispatcherType;

import org.apache.log4j.Logger;
import org.eclipse.jetty.servlet.ServletHolder;

public class ServletContextHandler extends org.eclipse.jetty.servlet.ServletContextHandler {

	private static final Logger LOG = Logger.getLogger(ServletContextHandler.class);
	private static final String FILE_PROTOCOL = "file:";
	private static final String JAR_FILE_SUFFIX = ".jar";

	public ServletContextHandler() {
		super(org.eclipse.jetty.servlet.ServletContextHandler.SESSIONS | org.eclipse.jetty.servlet.ServletContextHandler.SESSIONS);
		setClassLoader(ServletContextHandler.class.getClassLoader());
		URL[] urls = ((URLClassLoader) getClassLoader()).getURLs();
		Map<URI, List<String>> tldMap = new HashMap<>();
		for (int i = 0; i < urls.length; i++) {
			String urlStr = urls[i].toString();
			if (urlStr.startsWith(FILE_PROTOCOL) && urlStr.endsWith(JAR_FILE_SUFFIX)) {
				try {
					tldMap.put(urls[i].toURI(), null);
				} catch (URISyntaxException e) {
					LOG.error("unable to load jar: " + urlStr, e);
				}
			}
		}
		setAttribute("com.sun.appserv.tld.map", tldMap);
	}

	public void setServlets(Map<ServletHolder, String> servlets) {
		for (Entry<ServletHolder, String> cur : servlets.entrySet()) {
			addServlet(cur.getKey(), cur.getValue());
		}
	}

	public void setFilters(List<FilterHolder> holders) {
		for (FilterHolder cur : holders) {
			EnumSet<DispatcherType> types;
			if (cur.getTypes() == null || cur.getTypes().isEmpty()) {
				types = EnumSet.allOf(DispatcherType.class);
			} else {
				types = EnumSet.copyOf(cur.getTypes());
			}
			addFilter(new org.eclipse.jetty.servlet.FilterHolder(cur), cur.getPath(), types);
		}
	}

}
