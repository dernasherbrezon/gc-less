package com.aerse.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import com.aerse.util.LinkedQueueSet;

public class LinkedQueueSetTest {
	
	private LinkedQueueSet<String> set;
	
	@Test
	public void testAddRemoveUnique() {
		set.add("1");
		set.add("2");
		assertEquals("1", set.poll());
		assertEquals("2", set.poll());
		assertNull(set.poll());
	}
	
	@Test
	public void testAddUniqueOutOfOrder() {
		set.add("1");
		assertEquals("1", set.poll());
		set.add("2");
		set.add("3");
		assertEquals("2", set.poll());
		assertEquals("3", set.poll());
		assertNull(set.poll());
	}
	
	@Test
	public void testAddDuplicateOutOfOrder() {
		set.add("1");
		assertEquals("1", set.poll());
		set.add("2");
		set.add("3");
		assertEquals("2", set.poll());
		set.add("3");
		assertEquals("3", set.poll());
		assertNull(set.poll());
	}

	@Before
	public void start() {
		set = new LinkedQueueSet<>();
	}

}
