package com.aerse.gcless;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Iterator;

class NewLineDelimetedIterator implements Iterator<String> {

	private final BufferedReader r;
	private String curLine = null;

	NewLineDelimetedIterator(BufferedReader r) {
		this.r = r;
	}

	@Override
	public boolean hasNext() {
		try {
			curLine = r.readLine();
			if (curLine != null) {
				return true;
			} else {
				return false;
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String next() {
		return curLine;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException("unsupported opetation");
	}
}
