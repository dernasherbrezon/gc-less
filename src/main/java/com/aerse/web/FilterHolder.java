package com.aerse.web;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.beans.factory.annotation.Required;


public class FilterHolder implements Filter {

	private javax.servlet.Filter filter;
	private String path;
	private List<DispatcherType> types;
	private Map<String, String> params;
	
	public Map<String, String> getParams() {
		return params;
	}
	
	public void setParams(Map<String, String> params) {
		this.params = params;
	}

	public void setFilter(javax.servlet.Filter filter) {
		this.filter = filter;
	}
	
	@Required
	public javax.servlet.Filter getFilter() {
		return filter;
	}

	@Required
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public List<DispatcherType> getTypes() {
		return types;
	}

	public void setTypes(List<DispatcherType> types) {
		this.types = types;
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		filter.init(new com.aerse.web.FilterConfig(filterConfig, params));
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		filter.doFilter(request, response, chain);
	}

	@Override
	public void destroy() {
		filter.destroy();
	}
	
}
