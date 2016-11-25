package com.aerse.json;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * one char ahead
 * 
 */
public class JSONReader {

	public static boolean startArray(Reader reader, Cursor cursor) throws IOException {
		char next;
		if (cursor.getLastStopChar() == 0) {
			next = readNextSkippingWhitespace(reader, cursor);
		} else {
			next = cursor.getLastStopChar();
		}
		if (next == 'n') {
			skipNull(reader, cursor);
			cursor.setNullObject(true);
			return false;
		}
		if (next != '[') {
			throw new IllegalArgumentException("unexpected start: " + next);
		}
		next = readNextSkippingWhitespace(reader, cursor);
		cursor.setLastStopChar(next);
		if (next == ']') {
			return false;
		}
		return true;
	}

	public static void endArray(Reader reader, Cursor cursor) throws IOException {
		if (cursor.isNullObject()) {
			cursor.setNullObject(false);
			return;
		}
		if (cursor.getLastStopChar() != ']') {
			throw new IllegalArgumentException("unexpected end: " + cursor.getLastStopChar());
		}
		skipComma(reader, cursor);
	}

	public static boolean startObject(Reader reader, Cursor cursor) throws IOException {
		char next;
		if (cursor.getLastStopChar() == 0) {
			next = readNextSkippingWhitespace(reader, cursor);
		} else {
			next = cursor.getLastStopChar();
		}
		if (next == 'n') {
			skipNull(reader, cursor);
			cursor.setNullObject(true);
			return false;
		}
		if (next != '{') {
			throw new IllegalArgumentException("unexpected start: " + next);
		}
		next = readNextSkippingWhitespace(reader, cursor);
		cursor.setLastStopChar(next);
		if (next == '}') {
			return false;
		}
		return true;
	}

	public static void endObject(Reader reader, Cursor cursor) throws IOException {
		if (cursor.isNullObject()) {
			cursor.setNullObject(false);
			return;
		}
		if (cursor.getLastStopChar() != '}') {
			throw new IllegalArgumentException("unexpected end: " + cursor.getLastStopChar());
		}
		skipComma(reader, cursor);
	}

	private static void skipComma(Reader reader, Cursor cursor) throws IOException {
		char next = readNextSkippingWhitespace(reader, cursor);
		if (next == ',') {
			next = readNextSkippingWhitespace(reader, cursor);
		}
		cursor.setLastStopChar(next);
	}

	public static void skipValue(Reader reader, Cursor cursor) throws IOException {
		char next = cursor.getLastStopChar();
		if (next == '"') {
			skipString(next, reader, cursor);
		} else if (next == '{') {
			skipObject(reader, cursor);
		} else if (next == '[') {
			skipArray(reader, cursor);
		} else {
			// skip boolean, null, number
			char cur;
			do {
				cur = readChar(reader, cursor);
				if (",:]}/\\\"[{;=#".indexOf(cur) >= 0) {
					if (cur == ',') {
						cur = readNextSkippingWhitespace(reader, cursor);
					}
					cursor.setLastStopChar(cur);
					break;
				}
			} while (!cursor.isEndOfStreamReached());
		}
	}

	private static void skipObject(Reader reader, Cursor cursor) throws IOException {
		if (startObject(reader, cursor)) {
			while (cursor.getLastStopChar() != '}') {
				skipValue(reader, cursor);
			}
		}
		endObject(reader, cursor);
	}

	private static void skipArray(Reader reader, Cursor cursor) throws IOException {
		if (startArray(reader, cursor)) {
			while (cursor.getLastStopChar() != ']') {
				skipValue(reader, cursor);
			}
		}
		endArray(reader, cursor);
	}

	public static Integer readInteger(Reader reader, Cursor cursor) throws IOException {
		String str = readNumber(reader, cursor);
		if (str == null || str.length() == 0) {
			return null;
		}
		return Integer.valueOf(str);
	}

	public static Long readLong(Reader is, Cursor cursor) throws IOException {
		String str = readNumber(is, cursor);
		if (str == null || str.length() == 0) {
			return null;
		}
		return Long.valueOf(str);
	}

	private static String readNumber(Reader reader, Cursor cursor) throws IOException {
		if (cursor.getLastStopChar() == 'n') {
			skipNull(reader, cursor);
			return null;
		}
		StringBuilder b = new StringBuilder();
		b.append(cursor.getLastStopChar());
		char cur;
		do {
			cur = readChar(reader, cursor);
			if (Character.isWhitespace(cur)) {
				continue;
			}
			if (",:]}/\\\"[{;=#".indexOf(cur) >= 0) {
				if (cur == ',') {
					cur = readNextSkippingWhitespace(reader, cursor);
				}
				cursor.setLastStopChar(cur);
				break;
			}
			b.append(cur);
		} while (!cursor.isEndOfStreamReached());
		return b.toString();
	}

	public static Float readFloat(Reader is, Cursor currentPosition) throws IOException {
		String number = readNumber(is, currentPosition);
		if (number == null || number.length() == 0) {
			return null;
		}
		return Float.valueOf(number);
	}

	public static Double readDouble(Reader is, Cursor currentPosition) throws IOException {
		String number = readNumber(is, currentPosition);
		if (number == null || number.length() == 0) {
			return null;
		}
		return Double.valueOf(number);
	}

	public static Boolean readBoolean(Reader is, Cursor cursor) throws IOException {
		String number = readNumber(is, cursor);
		if (number == null || number.length() == 0) {
			return null;
		}
		return Boolean.valueOf(number);
	}

	public static List<Integer> readIntegerArray(Reader reader, Cursor cursor) throws IOException {
		List<Integer> result;
		if (!startArray(reader, cursor)) {
			result = Collections.emptyList();
		} else {
			result = new ArrayList<Integer>();
			do {
				result.add(readInteger(reader, cursor));
			} while (cursor.getLastStopChar() != ']');
		}
		endArray(reader, cursor);
		return result;
	}

	public static List<Boolean> readBooleanArray(Reader reader, Cursor cursor) throws IOException {
		List<Boolean> result;
		if (!startArray(reader, cursor)) {
			result = Collections.emptyList();
		} else {
			result = new ArrayList<Boolean>();
			do {
				result.add(readBoolean(reader, cursor));
			} while (cursor.getLastStopChar() != ']');
		}
		endArray(reader, cursor);
		return result;
	}

	public static List<String> readStringArray(Reader reader, Cursor cursor) throws IOException {
		List<String> result;
		if (!startArray(reader, cursor)) {
			result = Collections.emptyList();
		} else {
			result = new ArrayList<String>();
			do {
				result.add(readString(reader, cursor));
			} while (cursor.getLastStopChar() != ']');
		}
		endArray(reader, cursor);
		return result;
	}
	
	public static Set<String> readStringSet(Reader reader, Cursor cursor) throws IOException {
		Set<String> result;
		if (!startArray(reader, cursor)) {
			result = Collections.emptySet();
		} else {
			result = new HashSet<String>();
			do {
				result.add(readString(reader, cursor));
			} while (cursor.getLastStopChar() != ']');
		}
		endArray(reader, cursor);
		return result;
	}


	public static List<Long> readLongArray(Reader reader, Cursor cursor) throws IOException {
		List<Long> result;
		if (!startArray(reader, cursor)) {
			result = Collections.emptyList();
		} else {
			result = new ArrayList<Long>();
			do {
				result.add(readLong(reader, cursor));
			} while (cursor.getLastStopChar() != ']');
		}
		endArray(reader, cursor);
		return result;
	}

	private static void skipString(final char quote, Reader reader, Cursor cursor) throws IOException {
		for (;;) {
			char c = readChar(reader, cursor);
			switch (c) {
			case 0:
			case '\n':
			case '\r':
				throw new IllegalStateException("Unterminated string");
			case '\\':
				readChar(reader, cursor);
				break;
			default:
				if (c == quote) {
					// skip next delimeter
					skipComma(reader, cursor);
					return;
				}
			}
		}
	}

	public static String readString(Reader reader, Cursor cursor) throws IOException {
		char quote = cursor.getLastStopChar();
		if (quote == 'n') {
			skipNull(reader, cursor);
			return null;
		}
		if (quote != '"') {
			throw new IllegalArgumentException("unexpected string symbol: " + quote + " exptected: \"");
		}
		char c;
		StringBuilder sb = new StringBuilder();
		for (;;) {
			c = readChar(reader, cursor);
			switch (c) {
			case 0:
			case '\n':
			case '\r':
				throw new IllegalStateException("Unterminated string");
			case '\\':
				c = readChar(reader, cursor);
				switch (c) {
				case 'b':
					sb.append('\b');
					break;
				case 't':
					sb.append('\t');
					break;
				case 'n':
					sb.append('\n');
					break;
				case 'f':
					sb.append('\f');
					break;
				case 'r':
					sb.append('\r');
					break;
				case 'u':
					sb.append((char) Integer.parseInt(next(4, reader, cursor), 16));
					break;
				case 'x':
					sb.append((char) Integer.parseInt(next(2, reader, cursor), 16));
					break;
				default:
					sb.append(c);
				}
				break;
			default:
				if (c == quote) {
					skipComma(reader, cursor);
					return sb.toString();
				}
				sb.append(c);
			}
		}
	}

	private static void skipNull(Reader reader, Cursor cursor) throws IOException {
		skip(3, reader, cursor);
		skipComma(reader, cursor);
	}

	private static void skip(int n, Reader reader, Cursor cursor) throws IOException {
		for (int i = 0; i < n; i++) {
			reader.read();
		}
		cursor.addToPosition(n);
	}

	private static String next(int n, Reader reader, Cursor cursor) throws IOException {
		char[] buffer = new char[n];
		int pos = 0;

		int len;
		while (pos < n && (len = reader.read(buffer, pos, n - pos)) != -1) {
			pos += len;
		}
		cursor.addToPosition(pos);
		if (pos < n) {
			throw new IOException("Substring bounds error");
		}
		return new String(buffer);
	}

	public static StringBuilder readTag(Reader reader, Cursor cursor) throws IOException {
		if (cursor.getLastStopChar() == '}') {
			return null;
		}
		if (cursor.getLastStopChar() != '"') {
			char quote = readNextSkippingWhitespace(reader, cursor);
			if (cursor.isEndOfStreamReached()) {
				return null;
			}
			if (quote != '"') {
				throw new IOException("invalid format: expected quote before tag");
			}
		}
		StringBuilder b = new StringBuilder();
		char cur;
		do {
			cur = readChar(reader, cursor);
			if (cur == '"') {
				break;
			}
			b.append(cur);
		} while (!cursor.isEndOfStreamReached());
		char colon = readNextSkippingWhitespace(reader, cursor);
		if (colon != ':') {
			throw new IOException("invalid format: expect colon after tag: " + b.toString());
		}
		cursor.setLastStopChar(readNextSkippingWhitespace(reader, cursor));
		return b;
	}

	private static char readNextSkippingWhitespace(Reader reader, Cursor cursor) throws IOException {
		char result;
		do {
			result = readChar(reader, cursor);
			if (!Character.isWhitespace(result)) {
				break;
			}
		} while (!cursor.isEndOfStreamReached());
		return result;
	}

	private static char readChar(Reader reader, Cursor cursor) throws IOException {
		int result = reader.read();
		if (result == -1) {
			cursor.setEndOfStreamReached(true);
		} else {
			cursor.addToPosition(1);
		}
		return (char) result;
	}

}
