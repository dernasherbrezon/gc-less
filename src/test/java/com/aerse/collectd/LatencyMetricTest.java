package com.aerse.collectd;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.aerse.collectd.LatencyMetric;

public class LatencyMetricTest {

	private LatencyMetric metric;

	@Test
	public void averageGauge() {
		metric.add(10);
		metric.add(30);
		assertEquals(20, metric.get());
		metric.add(30);
		metric.add(10);
		assertEquals(40, metric.get());
	}

	@Test
	public void onlyOneValue() {
		metric.add(20);
		assertEquals(20l, metric.get());
	}

	@Test
	public void gauge() {
		metric.add(19);
		metric.get();
		assertEquals(19, metric.get());
	}

	@Before
	public void start() {
		metric = new LatencyMetric();
	}

}
