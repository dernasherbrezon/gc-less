package com.aerse.bootstrap;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.log4j.Logger;

public class LiveThread implements Runnable {

	private final static Logger LOG = Logger.getLogger(LiveThread.class);
	private Thread t;
	private boolean needToStop = false;
	private final Object monitorStop = new Object();

	@PostConstruct
	public void start() {
		t = new Thread(this, "LIVE-THREAD");
		t.setDaemon(false);
		t.start();
	}

	@PreDestroy
	public void stop() {
		if (t != null) {
			synchronized (monitorStop) {
				needToStop = true;
				monitorStop.notifyAll();
			}
		}
	}

	@Override
	public void run() {
		while (true) {
			try {
				synchronized (monitorStop) {
					if (needToStop) {
						return;
					}
					try {
						monitorStop.wait();
					} catch (InterruptedException e) {
						LOG.info("interrupted live thread. going to stop", e);
						return;
					}
				}
			} catch (Exception e) {
				LOG.error("exception in live thread", e);
			}
		}
	}

}
