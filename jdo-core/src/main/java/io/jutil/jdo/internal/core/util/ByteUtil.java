package io.jutil.jdo.internal.core.util;

/**
 * @author Jin Zheng
 * @since 2022-08-09
 */
public class ByteUtil {
	private static final int RADIX = 16;
	private static final int HEX_TO_BIT = 4;
	private static final int SHIFT_BYTE_TO_BIT = 3;
	private static final int BYTE_MASK = 0xff;

	private ByteUtil() {
	}

	public static String toHexString(byte[] bytes) {
		if (bytes == null || bytes.length == 0) {
			return null;
		}
		var builder = new StringBuilder(bytes.length << 1);
		for (var b : bytes) {
			appendHexString(builder, b);
		}
		return builder.toString();
	}

	public static void appendHexString(StringBuilder builder, byte val) {
		builder.append(Character.forDigit((val >>> HEX_TO_BIT) & 0xf, RADIX));
		builder.append(Character.forDigit(val & 0xf, RADIX));
	}

	public static byte[] toBytes(String hex) {
		var len = hex.length();
		if ((len & 1) != 0) {
			throw new IllegalArgumentException("无效16进制字符串");
		}

		var bytes = new byte[len >>> 1];
		for (int i = 0, j = 0; i < len; i += 2, j++) {
			var b = toByte(hex, i);
			bytes[j] = b;
		}
		return bytes;
	}

	public static byte toByte(String hex, int startIndex) {
		var high = Character.digit(hex.charAt(startIndex++), RADIX);
		var low = Character.digit(hex.charAt(startIndex), RADIX);
		return (byte) (((high << HEX_TO_BIT) | low) & BYTE_MASK);
	}

	public static void writeBytes(byte[] buffer, int startIndex, byte[] val) {
		System.arraycopy(val, 0, buffer, startIndex, val.length);
	}

	public static void writeShort(byte[] buffer, int startIndex, short val) {
		buffer[startIndex++] = (byte) ((val >> 8) & 0xff);
		buffer[startIndex] = (byte) (val & 0xff);
	}

	public static void writeInt(byte[] buffer, int startIndex, int val) {
		buffer[startIndex++] = (byte) ((val >> 24) & 0xff);
		buffer[startIndex++] = (byte) ((val >> 16) & 0xff);
		buffer[startIndex++] = (byte) ((val >> 8) & 0xff);
		buffer[startIndex] = (byte) (val & 0xff);
	}

	public static void writeLong(byte[] buffer, int startIndex, long val) {
		buffer[startIndex++] = (byte) ((val >> 56) & 0xff);
		buffer[startIndex++] = (byte) ((val >> 48) & 0xff);
		buffer[startIndex++] = (byte) ((val >> 40) & 0xff);
		buffer[startIndex++] = (byte) ((val >> 32) & 0xff);
		buffer[startIndex++] = (byte) ((val >> 24) & 0xff);
		buffer[startIndex++] = (byte) ((val >> 16) & 0xff);
		buffer[startIndex++] = (byte) ((val >> 8) & 0xff);
		buffer[startIndex] = (byte) (val & 0xff);
	}

	public static short readShort(byte[] buffer, int startIndex) {
		return (short) (((0xff & buffer[startIndex++]) << 8)
				| (0xff & buffer[startIndex]));
	}

	public static int readInt(byte[] buffer, int startIndex) {
		return ((0xff & buffer[startIndex++]) << 24)
				| ((0xff & buffer[startIndex++]) << 16)
				| ((0xff & buffer[startIndex++]) << 8)
				| (0xff & buffer[startIndex]);
	}

	public static long readLong(byte[] buffer, int startIndex) {
		return ((0xffL & buffer[startIndex++]) << 56)
				| ((0xffL & buffer[startIndex++]) << 48)
				| ((0xffL & buffer[startIndex++]) << 40)
				| ((0xffL & buffer[startIndex++]) << 32)
				| ((0xffL & buffer[startIndex++]) << 24)
				| ((0xffL & buffer[startIndex++]) << 16)
				| ((0xffL & buffer[startIndex++]) << 8)
				| (0xffL & buffer[startIndex]);
	}

}
