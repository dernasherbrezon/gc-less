package com.aerse.logging;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;

@ManagedResource
public class DynamicLogger {

	@ManagedOperation
	public String changeLogLevel(String category, String level) {
		Logger log = Logger.getLogger(category);
		log.setLevel(Level.toLevel(level));
		return "OK";
	}
}
