package com.aerse.mvc;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.log4j.Logger;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringSecurityDelegatingFilter implements Filter {

	private final static Logger LOG = Logger.getLogger(SpringSecurityDelegatingFilter.class);
	
	private Filter delegate;
	private AbstractApplicationContext ctx;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		String beanNameToDelegate = filterConfig.getInitParameter("delegate");
		if (beanNameToDelegate == null || beanNameToDelegate.trim().length() == 0) {
			throw new ServletException("bean name to delegate cannot be null");
		}
		AbstractApplicationContext springCtx = (AbstractApplicationContext) filterConfig.getServletContext().getAttribute(ClassPathXmlApplicationContext.class.getName() + ".ROOT");
		if (springCtx == null) {
			LOG.info("Application context not initialized. Do it");
			String contextPath = filterConfig.getInitParameter("context");
			if (contextPath == null || contextPath.trim().length() == 0) {
				throw new ServletException("context parameter should be specified");
			}
			this.ctx = new ClassPathXmlApplicationContext(contextPath);
			filterConfig.getServletContext().setAttribute(ClassPathXmlApplicationContext.class.getName() + ".ROOT", this.ctx);
			springCtx = this.ctx;
		}
		delegate = (Filter) springCtx.getBean(beanNameToDelegate);
		delegate.init(filterConfig);
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		delegate.doFilter(request, response, chain);
	}

	@Override
	public void destroy() {
		if (delegate != null) {
			delegate.destroy();
		}
		if( ctx != null ) {
			ctx.close();
		}
	}

}
