package io.jutil.jdo.internal.core.util;

/**
 * @author Jin Zheng
 * @since 2022-08-09
 */
public class ByteUtil {

	private ByteUtil() {
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
