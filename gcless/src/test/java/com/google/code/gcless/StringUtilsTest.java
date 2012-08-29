package com.google.code.gcless;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;

import org.junit.Test;

public class StringUtilsTest {

	@Test
	public void replaceWithSubToken() {
		assertEquals("hello two world", StringUtils.replaceToken("hello two two tokens world", "two tokens").toString());
	}

	@Test
	public void replaceTwoInARowTokens() {
		assertEquals("hello world", StringUtils.replaceToken("hello two tokens two tokens world", "two tokens").toString());
	}

	@Test
	public void replaceAll() {
		assertEquals("", StringUtils.replaceToken("hello world hello world hello world", "hello world").toString());
	}

	@Test
	public void tokenBiggerThanString() {
		assertEquals("hello", StringUtils.replaceToken("hello", "hello1").toString());
	}

	@Test
	public void noReplaceStart() {
		assertEquals("two token hello", StringUtils.replaceToken("two token hello", "two tokens").toString());
	}

	@Test
	public void noReplaceEnd() {
		assertEquals("hello two token", StringUtils.replaceToken("hello two token", "two tokens").toString());
	}

	@Test
	public void noReplaceMiddle() {
		assertEquals("hello two token world", StringUtils.replaceToken("hello two token world", "two tokens").toString());
	}

	@Test
	public void replaceStart() {
		assertEquals("hello", StringUtils.replaceToken("two tokens hello", "two tokens").toString());
	}

	@Test
	public void replaceEnd() {
		assertEquals("hello", StringUtils.replaceToken("hello two tokens", "two tokens").toString());
	}

	@Test
	public void replaceMiddle() {
		assertEquals("hello world", StringUtils.replaceToken("hello two tokens world", "two tokens").toString());
	}

	@Test
	public void replaceCombined() {
		assertEquals("hello world", StringUtils.replaceToken("two tokens hello two tokens world two tokens", "two tokens").toString());
	}

	@Test
	public void replaceStartOnlyToken() {
		assertEquals("two tokenshello", StringUtils.replaceToken("two tokenshello", "two tokens").toString());
	}

	@Test
	public void replaceEndOnlyToken() {
		assertEquals("hellotwo tokens", StringUtils.replaceToken("hellotwo tokens", "two tokens").toString());
	}

	@Test
	public void replaceMiddleOnlyToken() {
		assertEquals("hellotwo tokensworld", StringUtils.replaceToken("hellotwo tokensworld", "two tokens").toString());
	}

	@Test
	public void replaceMiddleOnlyToken2() {
		assertEquals("hello two tokensworld", StringUtils.replaceToken("hello two tokensworld", "two tokens").toString());
	}

	@Test
	public void replaceMiddleOnlyToken3() {
		assertEquals("hellotwo tokens world", StringUtils.replaceToken("hellotwo tokens world", "two tokens").toString());
	}

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
	public void splitSingleWord() {
		Iterable<String> result = StringUtils.splitBySpace("test");
		Iterator<String> it = result.iterator();
		assertTrue(it.hasNext());
		assertEquals("test", it.next());
		assertFalse(it.hasNext());
	}
}
