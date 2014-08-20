package com.google.code.gcless;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

public class CharStreamsTest {

	@Test
	public void testFixedSizeWithUpToToken2() {
		Iterator<String> it = CharStreams.readLines(new StringReader("1234ab123ab6789"), 5, "ab");
		assertTrue(it.hasNext());
		assertEquals("1234ab123ab", it.next());
		assertTrue(it.hasNext());
		assertEquals("6789", it.next());
	}

	@Test
	public void testFixedSizeWithUpToToken() {
		Iterator<String> it = CharStreams.readLines(new StringReader("123456789ab123456789"), 5, "ab");
		assertTrue(it.hasNext());
		assertEquals("123456789ab", it.next());
		assertTrue(it.hasNext());
		assertEquals("123456789", it.next());
	}
	
	@Test
	public void testLazyStringReader() {
		Iterator<String> str = CharStreams
				.readLines(new StringReader("1\n2\n3"));
		List<String> result = new ArrayList<String>();
		while (str.hasNext()) {
			result.add(str.next());
		}
		assertEquals(Arrays.asList("1", "2", "3"), result);
	}
}
