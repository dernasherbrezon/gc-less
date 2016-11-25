package com.aerse.util;

import org.apache.log4j.Logger;

public abstract class SafeRunnable implements Runnable {

	private static final Logger LOG = Logger.getLogger(SafeRunnable.class);

	@Override
	public void run() {
		try {
			safeRun();
		} catch (Exception e) {
			if (hasInterruptedCause(e)) {
				LOG.info("unable to continue: " + e.getMessage());
			} else {
				LOG.error("unable to run", e);
			}
		}
	}

	private static boolean hasInterruptedCause(Throwable e) {
		if (e instanceof InterruptedException) {
			return true;
		}
		if (e.getCause() != null) {
			return hasInterruptedCause(e.getCause());
		}
		return false;
	}

	public abstract void safeRun();

}
