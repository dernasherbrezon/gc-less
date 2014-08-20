package com.google.code.gcless;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CharStreams implements Iterator<String> {

	private static final int BUF_SIZE = 0x800; // 2K chars (4K bytes)
	private final BufferedReader r;
	private String curLine = null;

	public static Iterator<String> readLines(Reader r) {
		return new CharStreams(getBufferedReader(r));
	}

	private static BufferedReader getBufferedReader(Reader r) {
		BufferedReader reader;
		if (r instanceof BufferedReader) {
			reader = (BufferedReader) r;
		} else {
			reader = new BufferedReader(r);
		}
		return reader;
	}

	public static List<String> readAllLines(Reader r) throws IOException {
		List<String> result = new ArrayList<String>();
		BufferedReader reader = getBufferedReader(r);
		String line;
		while ((line = reader.readLine()) != null) {
			result.add(line);
		}
		return result;
	}

	public static String toString(Reader r) throws IOException {
		StringBuilder sb = new StringBuilder();
		copy(r, sb);
		return sb.toString();
	}

	public static long copy(Readable from, Appendable to) throws IOException {
		CharBuffer buf = CharBuffer.allocate(BUF_SIZE);
		long total = 0;
		while (from.read(buf) != -1) {
			buf.flip();
			to.append(buf);
			total += buf.remaining();
			buf.clear();
		}
		return total;
	}

	private CharStreams(BufferedReader r) {
		this.r = r;
	}

	@Override
	public boolean hasNext() {
		try {
			curLine = r.readLine();
			if (curLine != null) {
				return true;
			} else {
				return false;
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String next() {
		return curLine;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException("unsupported opetation");
	}

}
