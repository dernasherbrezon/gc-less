package com.aerse.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface FastController {

	String handleRequest(HttpServletRequest req, HttpServletResponse resp) throws Exception;

	String getRequestMappingURL();
}
