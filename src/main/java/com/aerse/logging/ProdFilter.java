package com.aerse.logging;

import org.apache.log4j.spi.Filter;
import org.apache.log4j.spi.LoggingEvent;

public class ProdFilter extends Filter {

	private String env;
	
	public void setEnv(String env) {
		this.env = env;
	}
	
	public String getEnv() {
		return env;
	}
	
	@Override
	public int decide(LoggingEvent event) {
		if( env == null || !env.equals("prod") ) {
			return Filter.DENY;
		}
		return Filter.NEUTRAL;
	}

}
