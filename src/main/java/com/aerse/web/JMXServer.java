package com.aerse.web;

import java.lang.management.ManagementFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.eclipse.jetty.jmx.MBeanContainer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.log.Log;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Service;

@Service
public class JMXServer {

	private Server server;
	
	@PostConstruct
	public void start() throws Exception {
		MBeanContainer mbContainer=new MBeanContainer(ManagementFactory.getPlatformMBeanServer());
        server.getContainer().addEventListener(mbContainer);
        server.addBean(mbContainer);
        mbContainer.addBean(Log.getLogger(JMXServer.class));
        server.start();
	}
	
	@PreDestroy
	public void stop() throws Exception {
		server.stop();
	}
	
	@Required
	public void setServer(Server server) {
		this.server = server;
	}
	
}
