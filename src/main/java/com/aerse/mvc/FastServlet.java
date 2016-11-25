package com.aerse.mvc;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.FileCopyUtils;

public class FastServlet extends HttpServlet implements ApplicationContextAware {

	private static final long serialVersionUID = 7845718143635303256L;

	private final static int MAX_RECURSIVE_FORWARDS = 5;
	public static final String INCLUDE_REQUEST_URI_ATTRIBUTE = "javax.servlet.include.request_uri";
	private static final Logger LOG = Logger.getLogger(FastServlet.class);
	private ApplicationContext context;

	private final Map<String, FastController> controllers = new HashMap<String, FastController>();
	private final Map<String, LastModifiedController> lastModified = new HashMap<String, LastModifiedController>();
	private final Map<String, HEADController> headControllers = new HashMap<>();

	@PostConstruct
	public void start() {
		Map<String, FastController> controllersPerBeanName = context.getBeansOfType(FastController.class);
		for (Entry<String, FastController> curEntry : controllersPerBeanName.entrySet()) {
			FastController controller = curEntry.getValue();
			if (controller.getRequestMappingURL().charAt(0) != '/') {
				LOG.error("Invalid mapping for controller. Should be mapped to absolute path. Got: " + controller.getRequestMappingURL());
			} else {
				LOG.info("Mapped controller bean: " + curEntry.getKey() + " to url: " + controller.getRequestMappingURL());
			}
			FastController oldValue = controllers.put(controller.getRequestMappingURL(), controller);
			if (oldValue != null) {
				LOG.error("double mapping for url path: " + oldValue.getRequestMappingURL() + " controller1: " + oldValue + " controller2: " + controller);
			}
			if (controller instanceof LastModifiedController) {
				lastModified.put(controller.getRequestMappingURL(), (LastModifiedController) controller);
			}
			if (controller instanceof HEADController) {
				headControllers.put(controller.getRequestMappingURL(), (HEADController) controller);
			}
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		process(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		process(req, resp);
	}
	
	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		process(req, resp);
	}
	
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		process(req, resp);
	}

	@Override
	protected void doHead(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HEADController headController = headControllers.get(req.getRequestURI());
		if (headController != null) {
			headController.handleHEAD(req, resp);
		} else {
			super.doHead(req, resp);
		}
	}

	@Override
	protected long getLastModified(HttpServletRequest req) {
		LastModifiedController lastModifiedController = lastModified.get(req.getRequestURI());
		if (lastModifiedController == null) {
			return -1;
		}
		return lastModifiedController.getLastModified(req);
	}

	private void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		process(req.getRequestURI(), null, req, resp, 0);
	}

	private void process(String uri, String previousURI, HttpServletRequest req, HttpServletResponse resp, int recursionLevel) throws ServletException, IOException {
		if (recursionLevel == MAX_RECURSIVE_FORWARDS) {
			throw new ServletException("too many forwards. URI: " + req.getRequestURI());
		}
		String includeURI = (String) req.getAttribute(INCLUDE_REQUEST_URI_ATTRIBUTE);
		if (includeURI != null && (previousURI == null || !previousURI.equals(includeURI))) {
			uri = includeURI;
		}
		FastController handler = controllers.get(uri);
		if (handler == null) {
			int queryIndex = uri.indexOf(";");
			if (queryIndex != -1) {
				uri = uri.substring(0, queryIndex);
				handler = controllers.get(uri);
			}
		}
		if (handler != null) {
			try {
				String result = handler.handleRequest(req, resp);
				if (result == null) {
					if (LOG.isDebugEnabled()) {
						LOG.debug("No view for: " + req.getRequestURI());
					}
					return;
				}
				process(result, uri, req, resp, recursionLevel + 1);
			} catch (Exception e) {
				throw new ServletException(e);
			}
		} else {
			if (recursionLevel == 0) {
				String path = getServletContext().getRealPath(req.getRequestURI());
				if (path == null) {
					resp.sendError(404);
					return;
				}
				File resource = new File(path);
				if (resource.exists()) {
					if (resource.isDirectory()) {
						resp.sendError(403);
					} else {
						FileCopyUtils.copy(new FileInputStream(resource), resp.getOutputStream());
					}
				} else {
					resp.sendError(404);
				}
				return;
			}
			if (includeURI != null) {
				req.getRequestDispatcher(uri).include(req, resp);
			} else {
				req.getRequestDispatcher(uri).forward(req, resp);
			}
		}
	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.context = applicationContext;
	}

}
