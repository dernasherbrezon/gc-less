package com.aerse.web;

import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.apache.jasper.servlet.JspServlet;
import org.springframework.beans.factory.annotation.Required;

public class JSPServlet extends JspServlet {

	private static final long serialVersionUID = -5509113970067290820L;
	private Map<String, String> params;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(new com.aerse.web.ServletConfig(config, params));
	}
	
	@Required
	public void setParams(Map<String, String> params) {
		this.params = params;
	}
}
