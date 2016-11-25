package com.aerse.logging;

import java.io.PrintWriter;

public class Log4jPrintWriter extends PrintWriter {

	public Log4jPrintWriter(String logname) {
		super(new Log4jWriter(logname));
	}

}
