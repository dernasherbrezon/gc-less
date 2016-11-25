package com.aerse.web;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class LanguageFilter implements Filter {
	
	private String lang;
	private ResourceBundle bundle;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		String bundleName = filterConfig.getInitParameter("bundlename");
		if( bundleName == null || bundleName.trim().length() == 0 ) {
			throw new ServletException("cannot find init parameter: bundlename");
		}
		bundle = ResourceBundle.getBundle(bundleName);
		lang = Locale.getDefault().getLanguage();
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		request.setAttribute("lang", lang);
		request.setAttribute("resourcebundle", bundle);
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		// do nothing
	}
	
}
