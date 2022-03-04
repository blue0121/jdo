package io.jutil.jdo.internal.core.util;

/**
 * @author Jin Zheng
 * @since 2022-02-18
 */
public class NumberUtil {
	private NumberUtil() {
	}

	/**
	 * 判断类型是否为数字型(byte, short, int, long, float, double)
	 *
	 * @param clazz 类型
	 * @return true为数字型
	 */
	public static boolean isNumber(Class<?> clazz) {
		return clazz == byte.class || clazz == short.class || clazz == int.class || clazz == long.class || clazz == Byte.class
				|| clazz == Short.class || clazz == Integer.class || clazz == Long.class || clazz == float.class || clazz == double.class
				|| clazz == Float.class || clazz == Double.class;
	}

	/**
	 * 把 byte 数组转换为十六进制字符串
	 *
	 * @param bytes byte 数组
	 * @return 十六进制字符串，范围是00~ff，每个字节占用两个字符
	 */
	public static String toHexString(byte[] bytes) {
		if (bytes == null || bytes.length == 0) {
			return "";
		}
		StringBuilder sb = new StringBuilder(bytes.length * 2);
		for (byte b : bytes) {
			String hex = Integer.toHexString(b & 0xff);
			if (hex.length() < 2) {
				sb.append('0');
			}
			sb.append(hex);
		}
		return sb.toString();
	}

	/**
	 * 把 int 转换为十六进制字符串
	 *
	 * @param val int值
	 * @return 十六进制字符串，范围是0000~ffff
	 */
	public static String toHexString(short val) {
		String tmp = Integer.toHexString(val);
		StringBuilder sb = new StringBuilder("0000");
		sb.replace(4 - tmp.length(), 4, tmp);
		return sb.toString();
	}

	/**
	 * 把 int 转换为十六进制字符串
	 *
	 * @param val int值
	 * @return 十六进制字符串，范围是00000000~ffffffff
	 */
	public static String toHexString(int val) {
		String tmp = Integer.toHexString(val);
		StringBuilder sb = new StringBuilder("00000000");
		sb.replace(8 - tmp.length(), 8, tmp);
		return sb.toString();
	}

	/**
	 * 把 long 转换为十六进制字符串
	 *
	 * @param val long值
	 * @return 十六进制字符串，范围是0000000000000000~ffffffffffffffff
	 */
	public static String toHexString(long val) {
		String tmp = Long.toHexString(val);
		StringBuilder sb = new StringBuilder("0000000000000000");
		sb.replace(16 - tmp.length(), 16, tmp);
		return sb.toString();
	}

}
