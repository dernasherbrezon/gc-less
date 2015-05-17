package com.google.code.gcless;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;

import org.junit.Test;

public class CryptoUtilsTest {

	@Test
	public void compareToStreamingMD5() throws Exception {
		String data = "data";
		String md5 = CryptoUtils.md5(new ByteArrayInputStream(data.getBytes()));
		assertEquals("8d777f385d3dfec8815d20f7496026dc", md5);
		assertEquals(CryptoUtils.md5(data.getBytes()), md5);
	}
}
