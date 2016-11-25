package com.aerse.collectd;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.collectd.api.ValueList;
import org.collectd.mx.Collector;
import org.collectd.protocol.TypesDB;

public class NamedMetrics implements Collector {

	private final ConcurrentHashMap<String, LatencyMetric> metrics = new ConcurrentHashMap<String, LatencyMetric>();
	private String pluginName;

	public void add(String name, long latency) {
		LatencyMetric cur = metrics.get(name);
		if (cur == null) {
			cur = new LatencyMetric();
			cur.add(latency);
			LatencyMetric old = metrics.putIfAbsent(name, cur);
			if (old != null) {
				old.add(latency);
			}
		} else {
			cur.add(latency);
		}
	}

	@Override
	public long getIntervalSec() {
		return -1;
	}

	@Override
	public Iterator<List<ValueList>> iterator() {
		return new NamedMetricsIterator(this.get());
	}

	private List<ValueList> get() {
		List<ValueList> result = new ArrayList<ValueList>();
		for (Entry<String, LatencyMetric> cur : metrics.entrySet()) {
			result.add(createValue(cur.getKey(), cur.getValue().get()));
		}
		return result;
	}

	private ValueList createValue(String metric, long value) {
		ValueList data = new ValueList();
		data.setPlugin(pluginName);
		data.setPluginInstance("0");
		data.setType(TypesDB.NAME_COUNTER);
		data.setTypeInstance(metric);
		data.setTime(System.currentTimeMillis());
		data.addValue(value);
		return data;
	}
	
	public void setPluginName(String pluginName) {
		this.pluginName = pluginName;
	}
}
