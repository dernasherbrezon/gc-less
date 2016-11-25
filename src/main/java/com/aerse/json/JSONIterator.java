package com.aerse.json;

import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;

public abstract class JSONIterator<E> implements Iterator<E> {

	private final Reader reader;
	private final Cursor cursor;
	private boolean hasNext = false;

	public JSONIterator(Reader reader, Cursor cursor) {
		this.reader = reader;
		this.cursor = cursor;
		try {
			hasNext = JSONReader.startArray(reader, cursor);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean hasNext() {
		if (!hasNext) {
			try {
				JSONReader.endArray(reader, cursor);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		return hasNext;
	}

	@Override
	public E next() {
		E result;
		try {
			result = read(reader, cursor);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		if (cursor.getLastStopChar() == ']') {
			hasNext = false;
		}
		return result;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException("cannot remove from stream");
	}

	public abstract E read(Reader reader, Cursor cursor) throws IOException;

}
