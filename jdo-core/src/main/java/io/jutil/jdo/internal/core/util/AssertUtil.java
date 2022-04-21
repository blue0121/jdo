package io.jutil.jdo.internal.core.util;

import java.util.Collection;
import java.util.Map;

/**
 * @author Jin Zheng
 * @since 2022-02-16
 */
public class AssertUtil {
	private static final String STR_IS_REQUIRED = " 不能为空";

	private AssertUtil() {
	}

	public static void notNull(Object object, String name) {
		if (name == null) {
			name = "";
		}

		if (object == null) {
			throw new NullPointerException(name + STR_IS_REQUIRED);
		}
	}

	public static void positive(long num, String name) {
		if (name == null) {
			name = "";
		}

		if (num <= 0) {
			throw new IllegalArgumentException(name + " 小于等于0");
		}
	}

	public static void nonNegative(long num, String name) {
		if (name == null) {
			name = "";
		}

		if (num < 0) {
			throw new IllegalArgumentException(name + " 小于0");
		}
	}

	public static void notEmpty(String str, String name) {
		if (name == null) {
			name = "";
		}

		if (str == null || str.isEmpty()) {
			throw new NullPointerException(name + STR_IS_REQUIRED);
		}
	}

	public static void notEmpty(Collection<?> list, String name) {
		if (name == null) {
			name = "";
		}

		if (list == null || list.isEmpty()) {
			throw new NullPointerException(name + STR_IS_REQUIRED);
		}
	}

	public static void notEmpty(Map<?, ?> map, String name) {
		if (name == null) {
			name = "";
		}

		if (map == null || map.isEmpty()) {
			throw new NullPointerException(name + STR_IS_REQUIRED);
		}
	}

	public static <T> void notEmpty(T[] array, String name) {
		if (name == null) {
			name = "";
		}

		if (array == null || array.length == 0) {
			throw new NullPointerException(name + STR_IS_REQUIRED);
		}
	}

	public static void notEmpty(byte[] array, String name) {
		if (name == null) {
			name = "";
		}

		if (array == null || array.length == 0) {
			throw new NullPointerException(name + STR_IS_REQUIRED);
		}
	}

	public static void equal(long n1, long n2, String text) {
		if (n1 != n2) {
			throw new IllegalArgumentException(text);
		}
	}

	public static void isTrue(boolean exp, String text) {
		if (!exp) {
			throw new IllegalArgumentException(text);
		}
	}

	public static void isFalse(boolean exp, String text) {
		if (exp) {
			throw new IllegalArgumentException(text);
		}
	}
}
