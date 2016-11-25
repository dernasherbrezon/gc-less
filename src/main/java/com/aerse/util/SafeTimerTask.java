package com.aerse.util;

import java.util.TimerTask;

import org.apache.log4j.Logger;

public abstract class SafeTimerTask extends TimerTask {

	private final static Logger LOG = Logger.getLogger(SafeTimerTask.class);

	@Override
	public void run() {
		safeRunInNonManagedContainer();
	}

	private void safeRunInNonManagedContainer() {
		try {
			safeRun();
		} catch (Exception e) {
			LOG.error("unable to run", e);
		}
	}

	public abstract void safeRun();
}
