package com.aerse.logging;

import org.apache.log4j.Logger;

public class TimedFilterTest {

	public static void main(String[] args) throws Exception {

		Logger log = Logger.getLogger(TimedFilterTest.class);

		for (int i = 0; i < 1000000; i++) {
			Thread.sleep((long) (Math.random() * 100));
			log.debug("debug");
			log.info("info");
			log.error("error", new Exception("exception"));
		}
	}
}
