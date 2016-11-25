package com.aerse.collectd;

import org.collectd.mx.Collector;

public interface RoutedCollector extends Collector {

	Class<? extends Sender> getDestination();
	
}
