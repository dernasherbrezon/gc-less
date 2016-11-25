package com.aerse.collectd;

public class LatencyMetric {

	private long currentCount = 0;
	private long currentLatency = 0;

	private long total = 0;

	public synchronized void add(long latency) {
		currentCount++;
		currentLatency += latency;
	}

	public synchronized long get() {
		if (currentCount != 0) {
			long currentAverage = currentLatency / currentCount;
			total += currentAverage;
			currentCount = 0;
			currentLatency = 0;
		}
		return total;
	}

}
