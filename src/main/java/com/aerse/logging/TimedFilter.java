package com.aerse.logging;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.spi.Filter;
import org.apache.log4j.spi.LoggingEvent;

public class TimedFilter extends Filter {

	private Map<LogEventFingerprint, TimeInfo> fingerPrints = new HashMap<LogEventFingerprint, TimeInfo>();

	private String intervals;
	private long[] intervalsTime = new long[0];

	public void setIntervals(String intervals) {
		if (intervals == null || intervals.trim().length() == 0) {
			throw new IllegalArgumentException("intervals cannot be null");
		}
		String[] parts = intervals.split(",");
		intervalsTime = new long[parts.length];
		for (int i = 0; i < parts.length; i++) {
			intervalsTime[i] = Long.valueOf(parts[i]);
		}
		Arrays.sort(intervalsTime);
		this.intervals = intervals;
	}

	public String getIntervals() {
		return intervals;
	}

	@Override
	public int decide(LoggingEvent event) {
		if (intervalsTime.length == 0) {
			return Filter.NEUTRAL;
		}

		LogEventFingerprint f = new LogEventFingerprint(event);
		TimeInfo t = fingerPrints.get(f);
		if (t == null) {
			t = new TimeInfo();
			t.setLastInterval(0);
			t.setLastSendTime(event.timeStamp);
			fingerPrints.put(f, t);
			return Filter.NEUTRAL;
		}
		
		long nextInterval = getNextInterval(t.getLastInterval());
		if( event.timeStamp > t.getLastSendTime() + nextInterval ) {
			t.setLastInterval(nextInterval);
			t.setLastSendTime(event.timeStamp);
			return Filter.NEUTRAL;			
		}
		
		return Filter.DENY;
	}

	private long getNextInterval(long curInterval) {
		for (int i = 0; i < intervalsTime.length; i++) {
			if( intervalsTime[i] > curInterval ) {
				return intervalsTime[i];
			}
		}
		return intervalsTime[intervalsTime.length - 1];
	}

}
