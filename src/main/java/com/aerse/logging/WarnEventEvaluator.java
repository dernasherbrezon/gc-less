package com.aerse.logging;

import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.TriggeringEventEvaluator;

public class WarnEventEvaluator implements TriggeringEventEvaluator {
	
	@Override
	public boolean isTriggeringEvent(LoggingEvent event) {
		return event.getLevel().isGreaterOrEqual(Level.WARN);
	}
	
}
