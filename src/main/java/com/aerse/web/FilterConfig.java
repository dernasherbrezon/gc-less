package com.aerse.web;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;

class FilterConfig implements javax.servlet.FilterConfig {

	private final javax.servlet.FilterConfig impl;
	private final Map<String, String> additionalProps;

	FilterConfig(javax.servlet.FilterConfig impl, Map<String, String> additionalProps) {
		this.impl = impl;
		this.additionalProps = additionalProps;
	}

	@Override
	public String getFilterName() {
		return impl.getFilterName();
	}

	@Override
	public ServletContext getServletContext() {
		return impl.getServletContext();
	}

	@Override
	public String getInitParameter(String name) {
		String result = impl.getInitParameter(name);
		if (result == null && additionalProps != null) {
			return additionalProps.get(name);
		}
		return result;
	}

	@Override
	public Enumeration<String> getInitParameterNames() {
		Enumeration<String> implParams = impl.getInitParameterNames();
		if (additionalProps != null) {
			Set<String> result = new HashSet<>(additionalProps.size());
			result.addAll(additionalProps.keySet());
			while (implParams.hasMoreElements()) {
				result.add(implParams.nextElement());
			}
			return Collections.enumeration(result);
		} else {
			return implParams;
		}
	}

}
