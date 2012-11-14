package com.google.code.gcless;

import java.util.Locale;

public final class StringUtils {

	public static String trimToNull(String str) {
		if( str == null ) {
			return null;
		}
		String result = str.trim();
		if( result.length() == 0 ) {
			return null;
		}
		return result;
	}
	
	public static Iterable<String> splitBySpace(String str) {
		return new Splitter(str);
	}

	/**
	 * @see public static Appendable replaceToken(String original, String token,
	 *      boolean ignoreCase);
	 * @param original
	 * @param token
	 * @return
	 */
	public static Appendable replaceToken(String original, String token) {
		return replaceToken(original, token, false);
	}

	/**
	 * replace tokens from original string with "". For example: original -
	 * "two tokens hello two tokens worldtwo tokens two tokens" token -
	 * "two tokens" result - "hello worldtwo tokens"
	 * 
	 * @param original
	 *            - cannot be null
	 * @param token
	 * @param ignoreCase
	 * @return
	 */
	public static Appendable replaceToken(String original, String token, boolean ignoreCase) {
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

		String originalWithCaseApplied;
		String tokenWithCaseApplied;
		if (ignoreCase) {
			// doesnt matter which locale to use. Just make sure they are in the
			// same locale.
			originalWithCaseApplied = original.toLowerCase(Locale.UK);
			tokenWithCaseApplied = token.toLowerCase(Locale.UK);
		} else {
			originalWithCaseApplied = original;
			tokenWithCaseApplied = token;
		}

		StringBuilder result = new StringBuilder();
		boolean previousCharIsWhitespace = true;
		char firstChar = tokenWithCaseApplied.charAt(0);
		int lastTokenEndIndex = 0;
		for (int i = 0; i < originalLength;) {
			int tokenEndIndex = tokenLength + i;
			// do no check further. there is not enough chars in original to
			// match token
			if (tokenEndIndex > originalLength) {
				break;
			}
			char curChar = originalWithCaseApplied.charAt(i);
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
				curChar = originalWithCaseApplied.charAt(i + j);
				char tokenChar = tokenWithCaseApplied.charAt(j);
				if (tokenChar != curChar) {
					matched = false;
					break;
				}
			}

			if (!matched) {
				i++;
				previousCharIsWhitespace = false;
				continue;
			}

			// skip token from ending of string
			if (tokenEndIndex == originalLength) {
				int endIndex = 0;
				if (i - 1 > 0) {
					endIndex = i - 1;
				}
				result.append(original.substring(lastTokenEndIndex, endIndex));
				lastTokenEndIndex = tokenEndIndex;
				break;
			}

			if (Character.isWhitespace(originalWithCaseApplied.charAt(tokenEndIndex))) {
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
