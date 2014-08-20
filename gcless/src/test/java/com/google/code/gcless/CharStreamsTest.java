package com.google.code.gcless;

import static org.junit.Assert.assertEquals;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

public class CharStreamsTest {

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
