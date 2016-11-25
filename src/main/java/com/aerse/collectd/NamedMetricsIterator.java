package com.aerse.collectd;

import java.util.Iterator;
import java.util.List;

import org.collectd.api.ValueList;

public class NamedMetricsIterator implements Iterator<List<ValueList>> {

	private List<ValueList> data;

	public NamedMetricsIterator(List<ValueList> data) {
		this.data = data;
	}

	@Override
	public boolean hasNext() {
		if (this.data == null) {
			return false;
		}
		return true;
	}

	@Override
	public List<ValueList> next() {
		List<ValueList> result = data;
		this.data = null;
		return result;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

}
