package com.aerse.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Iterator;

import org.junit.Test;

import com.aerse.json.Cursor;
import com.aerse.json.JSONIterator;
import com.aerse.json.JSONReader;

public class JSONIteratorTest {

	@Test
	public void testEmptyArray() {
		StringReader r = new StringReader("[]");
		Iterator<String> it = new JSONStringIterator(r, new Cursor());
		assertFalse(it.hasNext());
	}
	
	@Test
	public void testNull() {
		StringReader r = new StringReader("null");
		Iterator<String> it = new JSONStringIterator(r, new Cursor());
		assertFalse(it.hasNext());
	}

	@Test
	public void testReadArray() {
		StringReader r = new StringReader("[\"1\", null, \"2\"]");
		Iterator<String> it = new JSONStringIterator(r, new Cursor());
		assertTrue(it.hasNext());
		assertEquals("1", it.next());
		assertTrue(it.hasNext());
		assertNull(it.next());
		assertTrue(it.hasNext());
		assertEquals("2", it.next());
		assertFalse(it.hasNext());
	}

	private static class JSONStringIterator extends JSONIterator<String> {

		public JSONStringIterator(Reader reader, Cursor cursor) {
			super(reader, cursor);
		}

		@Override
		public String read(Reader reader, Cursor cursor) throws IOException {
			return JSONReader.readString(reader, cursor);
		}
	}

}
