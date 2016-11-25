package com.aerse.util;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class PeriodTimer {

	private final static Logger LOG = Logger.getLogger(PeriodTimer.class);
	private Timer t;
	private String name;
	private boolean isDaemon;

	@PostConstruct
	public void start() {
		t = new Timer(name, isDaemon);
	}

	@PreDestroy
	public void stop() {
		if (t != null) {
			t.cancel();
		}
	}

	public void schedule(String taskName, TimerTask task, Date time) {
		LOG.info("schedule " + taskName + " at \"" + name + "\". Time: " + time);
		t.schedule(task, time);
	}

	public void schedule(String taskName, TimerTask task, long delay) {
		LOG.info("schedule " + taskName + " at \"" + name + "\". delay: " + delay);
		t.schedule(task, delay);
	}

	@Required
	public void setName(String name) {
		this.name = name;
	}

	@Required
	public void setDaemon(boolean isDaemon) {
		this.isDaemon = isDaemon;
	}

}
