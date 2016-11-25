package com.aerse.web;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;

class ServletConfig implements javax.servlet.ServletConfig {

	private final javax.servlet.ServletConfig impl;
	private final Map<String, String> params;

	ServletConfig(javax.servlet.ServletConfig impl, Map<String, String> params) {
		this.impl = impl;
		this.params = params;
	}

	@Override
	public String getServletName() {
		return impl.getServletName();
	}

	@Override
	public ServletContext getServletContext() {
		return impl.getServletContext();
	}

	@Override
	public String getInitParameter(String name) {
		String result = impl.getInitParameter(name);
		if (result != null) {
			return result;
		}
		return params.get(name);
	}

	@Override
	public Enumeration<String> getInitParameterNames() {
		Enumeration<String> implParams = impl.getInitParameterNames();
		if (params != null) {
			Set<String> result = new HashSet<>(params.size());
			result.addAll(params.keySet());
			while (implParams.hasMoreElements()) {
				result.add(implParams.nextElement());
			}
			return Collections.enumeration(result);
		} else {
			return implParams;
		}
	}

}
