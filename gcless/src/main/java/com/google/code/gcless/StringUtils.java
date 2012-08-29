package com.google.code.gcless;

public final class StringUtils {

	public static Iterable<String> splitBySpace(String str) {
		return new Splitter(str);
	}

	/**
	 * replace tokens from original string with "". For example: original -
	 * "two tokens hello two tokens worldtwo tokens two tokens" token -
	 * "two tokens" result - "hello worldtwo tokens"
	 * 
	 * @param original
	 *            - cannot be null
	 * @param token
	 * @return
	 */
	public static Appendable replaceToken(String original, String token) {
		if (original == null) {
			throw new IllegalArgumentException("original string cannot be null");
		}
		if (token == null || token.length() == 0) {
			return new StringBuilder(original);
		}
		int tokenLength = token.length();
		int originalLength = original.length();
		if (tokenLength > originalLength) {
			return new StringBuilder(original);
		}

		StringBuilder result = new StringBuilder();
		boolean previousCharIsWhitespace = true;
		char firstChar = token.charAt(0);
		int lastTokenEndIndex = 0;
		for (int i = 0; i < originalLength;) {
			int tokenEndIndex = tokenLength + i;
			//do no check further. there is not enough chars in original to match token
			if( tokenEndIndex > originalLength ) {
				break;
			}
			char curChar = original.charAt(i);
			if (curChar != firstChar) {
				if (!Character.isWhitespace(curChar)) {
					previousCharIsWhitespace = false;
				} else {
					previousCharIsWhitespace = true;
				}
				i++;
				continue;
			}

			// whitespace is delimeter for tokens
			if (!previousCharIsWhitespace) {
				i++;
				continue;
			}

			boolean matched = true;
			for (int j = 1; j < tokenLength && i + j < originalLength; j++) {
				curChar = original.charAt(i + j);
				char tokenChar = token.charAt(j);
				if (tokenChar != curChar) {
					matched = false;
					break;
				}
			}

			if (!matched) {
				i++;
				continue;
			}

			// skip token from ending of string
			if (tokenEndIndex == originalLength) {
				result.append(original.substring(lastTokenEndIndex, i - 1));
				lastTokenEndIndex = tokenEndIndex;
				break;
			}

			if (Character.isWhitespace(original.charAt(tokenEndIndex))) {
				// skip token from beginning of string
				if (i == 0) {
					lastTokenEndIndex = tokenEndIndex + 1;
					i += 2;
					continue;
				}

				if (lastTokenEndIndex < i - 1) {
					result.append(original.substring(lastTokenEndIndex, i - 1));
				}
				lastTokenEndIndex = tokenEndIndex;
				i += tokenLength;
			} else {
				i++;
			}
		}

		// append remaining
		if (lastTokenEndIndex != originalLength) {
			result.append(original.substring(lastTokenEndIndex));
		}
		return result;
	}

	// copy/paste from apache commons-lang
	public static boolean isBlank(String str) {
		int strLen;
		if (str == null || (strLen = str.length()) == 0) {
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			if ((Character.isWhitespace(str.charAt(i)) == false)) {
				return false;
			}
		}
		return true;
	}

	public static boolean isNotBlank(String str) {
		int strLen;
		if (str == null || (strLen = str.length()) == 0) {
			return false;
		}
		for (int i = 0; i < strLen; i++) {
			if ((Character.isWhitespace(str.charAt(i)) == false)) {
				return true;
			}
		}
		return false;
	}

	private StringUtils() {
		// do nothing
	}

}
