package com.aerse.gcless;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Iterator;

public class FixedSizeWithUpToTokenIterator implements Iterator<String> {

	private final BufferedReader r;
	private final char[] buf;
	private final String token;
	private String curLine = null;

	FixedSizeWithUpToTokenIterator(BufferedReader r, int minLength, String token) {
		this.r = r;
		this.buf = new char[minLength];
		this.token = token;
	}

	@Override
	public boolean hasNext() {
		try {
			int readChars = r.read(buf);
			if (readChars == -1) {
				return false;
			}
			if (readChars < buf.length) {
				curLine = new String(buf, 0, readChars);
				return true;
			}
			StringBuilder str = new StringBuilder();
			str.append(buf);
			int curSubstringIndex = -1;
			while (true) {
				int charInt = r.read();
				if (charInt == -1) {
					break;
				}
				char cur = (char) charInt;
				str.append(cur);
				if (token.charAt(curSubstringIndex + 1) == cur) {
					curSubstringIndex++;
				} else {
					curSubstringIndex = -1;
				}
				if (curSubstringIndex + 1 == token.length()) {
					break;
				}
			}
			curLine = str.toString();
			return true;
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
