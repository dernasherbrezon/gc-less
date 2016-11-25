package com.aerse.mvc;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class StaticController implements LastModifiedController, HEADController {

	private long lastModified = -1;
	
	@PostConstruct
	public void start() {
		lastModified = System.currentTimeMillis()/1000 * 1000;
	}
	
	@Override
	public long getLastModified(HttpServletRequest req) {
		return lastModified;
	}
	
	@Override
	public void handleHEAD(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html;charset=UTF-8");
	}
	
}
