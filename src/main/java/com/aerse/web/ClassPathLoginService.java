package com.aerse.web;

import java.net.URL;

import org.eclipse.jetty.security.HashLoginService;

public class ClassPathLoginService extends HashLoginService {

	@Override
	public void setConfig(String config) {
		String prefix = "classpath:";
		String result = config;
		if( config.startsWith(prefix) ) {
			URL resource = ClassPathLoginService.class.getClassLoader().getResource(config.substring(prefix.length()));
			if( resource == null ) {
				throw new IllegalStateException("unable to find config in classpath: " + config);
			}
			result = resource.toString();
		}
		super.setConfig(result);
	}
	
}
