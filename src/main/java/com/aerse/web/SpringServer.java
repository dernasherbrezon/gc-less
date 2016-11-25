package com.aerse.web;

import java.util.Collection;

import org.eclipse.jetty.server.Server;
import org.springframework.stereotype.Service;

@Service
public class SpringServer extends Server {

	public void setBeans(Collection<Object> beans) {
		for (Object o : beans) {
			addBean(o);
		}
	}

}
