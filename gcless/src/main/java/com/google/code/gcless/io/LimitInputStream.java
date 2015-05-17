package com.google.code.gcless.io;

import java.io.IOException;
import java.io.InputStream;

public class LimitInputStream extends InputStream {

	private final InputStream is;
	private final int limit;
	private int currentRead = 0;
	
	public LimitInputStream(InputStream is, int limit) {
		this.is = is;
		this.limit = limit;
	}
	
	@Override
	public int read() throws IOException {
		if( currentRead == limit ) {
			throw new LimitReachedException("limit reached: " + limit);
		}
		int result = is.read();
		currentRead++;
		return result;
	}
		
	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		if( currentRead > limit ) {
			throw new LimitReachedException("limit reached: " + limit);
		}
		int actualRead = is.read(b, off, len);
		currentRead += actualRead;
		return actualRead;
	}
	
	@Override
	public void close() throws IOException {
		is.close();
	}
	
}
