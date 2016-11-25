package com.aerse.mvc;

import javax.servlet.http.HttpServletRequest;

public interface LastModifiedController {

	long getLastModified(HttpServletRequest req);
	
}
