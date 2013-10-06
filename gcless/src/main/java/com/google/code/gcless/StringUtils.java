package com.google.code.gcless;

import java.util.Locale;

public final class StringUtils {

	// Mod-79 lookup table.
	private final static char[] table = { 1, 0, 160, 0, 0, 0, 0, 0, 0, 9, 10, 11, 12, 13, 0, 0, 8232, 8233, 0, 0, 0, 0, 0, 8239, 0, 0, 0, 0, 0, 0, 0, 0, 32, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 12288, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 133, 8192, 8193, 8194,
			8195, 8196, 8197, 8198, 8199, 8200, 8201, 8202, 0, 0, 0, 0, 0, 8287, 5760, 0, 0, 6158, 0, 0, 0 };

	public static String trimToNull(String str) {
		if( str == null ) {
			return null;
		}
		String result = trim(str);
		if( result.length() == 0 ) {
			return null;
		}
		return result;
	}
	
	public static String trim(CharSequence sequence) {
		int len = sequence.length();
		int first;
		int last;

		for (first = 0; first < len; first++) {
			if (!isWhitespace(sequence.charAt(first))) {
				break;
			}
		}
		for (last = len - 1; last > first; last--) {
			if (!isWhitespace(sequence.charAt(last))) {
				break;
			}
		}

		return sequence.subSequence(first, last + 1).toString();
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
			if ((isWhitespace(str.charAt(i)) == false)) {
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
			if ((isWhitespace(str.charAt(i)) == false)) {
				return true;
			}
		}
		return false;
	}
	
    public static boolean containsIgnoreCase(CharSequence str, CharSequence searchStr) {
        if (str == null || searchStr == null) {
            return false;
        }
        int len = searchStr.length();
        int max = str.length() - len;
        for (int i = 0; i <= max; i++) {
            if (regionMatches(str, true, i, searchStr, 0, len)) {
                return true;
            }
        }
        return false;
    }
    
    private static boolean regionMatches(CharSequence cs, boolean ignoreCase, int thisStart,
            CharSequence substring, int start, int length)    {
        if (cs instanceof String && substring instanceof String) {
            return ((String) cs).regionMatches(ignoreCase, thisStart, ((String) substring), start, length);
        } else {
            return cs.toString().regionMatches(ignoreCase, thisStart, substring.toString(), start, length);
        }
    }
    
	private static boolean isWhitespace(char c) {
		return table[c % 79] == c;
	}

	private StringUtils() {
		// do nothing
	}

}
