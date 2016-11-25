package com.aerse.gcless;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CryptoUtils {

	public static String md5(String data) {
		try {
			return md5(data.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("unable to calculate md5", e);
		}
	}

	public static String md5(byte[] data) {
		MessageDigest md5Alg;
		try {
			md5Alg = MessageDigest.getInstance("MD5");
			md5Alg.reset();
			byte[] digest = md5Alg.digest(data);
			BigInteger result = new BigInteger(1, digest);
			String resultStr = null;
			if (result.signum() < 0) {
				resultStr = result.negate().toString(16);
			} else {
				resultStr = result.toString(16);
			}
			while (resultStr.length() < 32) {
				resultStr = "0" + resultStr;
			}
			return resultStr;
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("unable to generate md5", e);
		}
	}

	public static String md5(InputStream is) throws Exception {
		MessageDigest md5Alg;
		md5Alg = MessageDigest.getInstance("MD5");
		md5Alg.reset();
		byte[] buf = new byte[2048];
		int curByte = -1;
		while ((curByte = is.read(buf)) != -1) {
			md5Alg.update(buf, 0, curByte);
		}
		byte[] digest = md5Alg.digest();
		BigInteger result = new BigInteger(1, digest);
		String resultStr = null;
		if (result.signum() < 0) {
			resultStr = result.negate().toString(16);
		} else {
			resultStr = result.toString(16);
		}
		while (resultStr.length() < 32) {
			resultStr = "0" + resultStr;
		}
		return resultStr;
	}
}
