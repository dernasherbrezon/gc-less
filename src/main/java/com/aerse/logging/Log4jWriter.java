package com.aerse.logging;

import java.io.IOException;
import java.io.Writer;

import org.apache.log4j.Logger;

public class Log4jWriter extends Writer {

	private final Logger log;
	
	public Log4jWriter(String logname) {
		log = Logger.getLogger(logname);
	}
	
	@Override
	public void write(char[] cbuf, int off, int len) throws IOException {
		log.info(String.valueOf(cbuf));
	}

	@Override
	public void flush() throws IOException {
		//do nothing
	}

	@Override
	public void close() throws IOException {
		//do nothing
	}

}
