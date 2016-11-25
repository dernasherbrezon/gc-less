package com.aerse.collectd;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.collectd.api.ValueList;
import org.springframework.beans.factory.annotation.Required;

public class UdpSender implements Sender {

	private static final Logger LOG = Logger.getLogger(UdpSender.class);
	private org.collectd.protocol.UdpSender impl;

	@Override
	public void dispatch(List<ValueList> data) {
		for (ValueList cur : data) {
			dispatch(cur);
		}
	}

	@Override
	public void dispatch(ValueList data) {
		impl.dispatch(data);
	}

	@Override
	public void flush() {
		try {
			impl.flush();
		} catch (IOException e) {
			LOG.error("unable to flush", e);
		}
	}

	@Required
	public void setImpl(org.collectd.protocol.UdpSender impl) {
		this.impl = impl;
	}
}
