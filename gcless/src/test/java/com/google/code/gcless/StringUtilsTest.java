package com.google.code.gcless;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;

import org.junit.Test;

public class StringUtilsTest {

	@Test
	public void splitEmptyString() {
		Iterable<String> result = StringUtils.splitBySpace("   ");
		Iterator<String> it = result.iterator();
		assertFalse(it.hasNext());
	}
	
	@Test
	public void splitNormalString() {
		Iterable<String> result = StringUtils.splitBySpace(" test  value ");
		Iterator<String> it = result.iterator();
		assertTrue(it.hasNext());
		assertEquals("test", it.next());
		assertTrue(it.hasNext());
		assertEquals("value", it.next());
		assertFalse(it.hasNext());
	}
	
	@Test
	public  void splitSingleWord() {
		Iterable<String> result = StringUtils.splitBySpace("test");
		Iterator<String> it = result.iterator();
		assertTrue(it.hasNext());
		assertEquals("test", it.next());
		assertFalse(it.hasNext());
	}
	
}
