package com.aerse.collectd;

import java.util.List;

import org.collectd.api.ValueList;

public interface Sender {

	void dispatch(List<ValueList> data);

	void dispatch(ValueList data);

	void flush();

}
