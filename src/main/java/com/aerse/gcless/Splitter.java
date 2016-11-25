package com.aerse.gcless;

import java.util.Iterator;
import java.lang.Iterable;

class Splitter implements Iterable<String>, Iterator<String> {

	private final String str;
	private int startToken = 0;

	Splitter(String str) {
		this.str = str;
	}

	@Override
	public boolean hasNext() {
		for (int i = startToken; i < str.length(); i++) {
			char cur = str.charAt(i);
			if (!Character.isWhitespace(cur)) {
				startToken = i;
				return true;
			}
		}
		return false;
	}

	@Override
	public String next() {
		for (int i = startToken; i < str.length(); i++) {
			char cur = str.charAt(i);
			if (Character.isWhitespace(cur)) {
				String result = str.substring(startToken, i);
				startToken = i;
				return result;
			}
			if (i == str.length() - 1) {
				String result = str.substring(startToken);
				startToken = i + 1;
				return result;
			}
		}
		return null;
	}

	@Override
	public void remove() {
		// not implemented
	}

	@Override
	public Iterator<String> iterator() {
		return this;
	}

}
