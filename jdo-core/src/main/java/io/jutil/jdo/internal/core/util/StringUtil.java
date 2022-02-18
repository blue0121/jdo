package io.jutil.jdo.internal.core.util;

import java.util.Collection;

/**
 * @author Jin Zheng
 * @since 2022-02-18
 */
public class StringUtil {
	private StringUtil() {
	}

	/**
	 * 获取Jdbc类型，jdbc:mysql://localhost:3306/yourDBName => mysql
	 *
	 * @param jdbcUrl
	 * @return
	 */
	public static String getJdbcType(String jdbcUrl) {
		if (jdbcUrl == null || jdbcUrl.isEmpty()) {
			return null;
		}

		int len = 5;
		int index = jdbcUrl.indexOf(":", len);
		if (index == -1) {
			return null;
		}

		String jdbcType = jdbcUrl.substring(len, index);
		return jdbcType;
	}

	/**
	 * 连接字符串
	 *
	 * @param list      字符串列表
	 * @param separator 分割符
	 * @return 连接后的字符串
	 */
	public static String join(Collection<?> list, String separator) {
		if (list == null || list.isEmpty()) {
			return null;
		}

		String sep = separator == null ? "" : separator;
		StringBuilder concat = new StringBuilder();
		for (Object obj : list) {
			concat.append(obj).append(sep);
		}
		if (concat.length() > sep.length()) {
			concat.delete(concat.length() - sep.length(), concat.length());
		}
		return concat.toString();
	}

	/**
	 * 产生重复字符串
	 *
	 * @param item      重复项
	 * @param times     重复次数
	 * @param separator 分割符
	 * @return 重复字符串
	 */
	public static String repeat(String item, int times, String separator) {
		AssertUtil.notEmpty(item, "重复项");
		AssertUtil.positive(times, "次数");

		String sep = separator == null ? "" : separator;
		StringBuilder concat = new StringBuilder();
		for (int i = 0; i < times; i++) {
			concat.append(item).append(sep);
		}
		if (concat.length() > sep.length()) {
			concat.delete(concat.length() - sep.length(), concat.length());
		}
		return concat.toString();
	}

}
