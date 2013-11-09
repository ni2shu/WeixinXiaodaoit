package co.xiaodao.weixin.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class EncodingUtil {

	public EncodingUtil() {
	}

	public static String utf8ToUnicode(String inStr) {
		char myBuffer[] = inStr.toCharArray();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < inStr.length(); i++) {
			Character.UnicodeBlock ub = Character.UnicodeBlock.of(myBuffer[i]);
			if (ub == Character.UnicodeBlock.BASIC_LATIN)
				sb.append(myBuffer[i]);
			else if (ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
				int j = myBuffer[i] - 65248;
				sb.append((char) j);
			} else {
				short s = (short) myBuffer[i];
				String hexS = Integer.toHexString(s);
				String unicode = (new StringBuilder("\\u")).append(hexS)
						.toString();
				sb.append(unicode.toLowerCase());
			}
		}

		return sb.toString();
	}

	public static String unicodeToUtf8(String theString) {
		int len = theString.length();
		StringBuffer outBuffer = new StringBuffer(len);
		for (int x = 0; x < len;) {
			char aChar = theString.charAt(x++);
			if (aChar == '\\') {
				aChar = theString.charAt(x++);
				if (aChar == 'u') {
					int value = 0;
					for (int i = 0; i < 4; i++) {
						aChar = theString.charAt(x++);
						switch (aChar) {
						case 48: // '0'
						case 49: // '1'
						case 50: // '2'
						case 51: // '3'
						case 52: // '4'
						case 53: // '5'
						case 54: // '6'
						case 55: // '7'
						case 56: // '8'
						case 57: // '9'
							value = ((value << 4) + aChar) - 48;
							break;

						case 97: // 'a'
						case 98: // 'b'
						case 99: // 'c'
						case 100: // 'd'
						case 101: // 'e'
						case 102: // 'f'
							value = ((value << 4) + 10 + aChar) - 97;
							break;

						case 65: // 'A'
						case 66: // 'B'
						case 67: // 'C'
						case 68: // 'D'
						case 69: // 'E'
						case 70: // 'F'
							value = ((value << 4) + 10 + aChar) - 65;
							break;

						case 58: // ':'
						case 59: // ';'
						case 60: // '<'
						case 61: // '='
						case 62: // '>'
						case 63: // '?'
						case 64: // '@'
						case 71: // 'G'
						case 72: // 'H'
						case 73: // 'I'
						case 74: // 'J'
						case 75: // 'K'
						case 76: // 'L'
						case 77: // 'M'
						case 78: // 'N'
						case 79: // 'O'
						case 80: // 'P'
						case 81: // 'Q'
						case 82: // 'R'
						case 83: // 'S'
						case 84: // 'T'
						case 85: // 'U'
						case 86: // 'V'
						case 87: // 'W'
						case 88: // 'X'
						case 89: // 'Y'
						case 90: // 'Z'
						case 91: // '['
						case 92: // '\\'
						case 93: // ']'
						case 94: // '^'
						case 95: // '_'
						case 96: // '`'
						default:
							throw new IllegalArgumentException(
									"Malformed   \\uxxxx   encoding.");
						}
					}

					outBuffer.append((char) value);
				} else {
					if (aChar == 't')
						aChar = '\t';
					else if (aChar == 'r')
						aChar = '\r';
					else if (aChar == 'n')
						aChar = '\n';
					else if (aChar == 'f')
						aChar = '\f';
					outBuffer.append(aChar);
				}
			} else {
				outBuffer.append(aChar);
			}
		}

		return outBuffer.toString();
	}

	public static String urlEncodeUTF8(String source) {
		String result = source;
		try {
			result = URLEncoder.encode(source, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static String urlEncodeGB2312(String source) {
		String result = source;
		try {
			result = URLEncoder.encode(source, "gb2312");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static String emoji(int hexEmoji) {
		return String.valueOf(Character.toChars(hexEmoji));
	}
}
