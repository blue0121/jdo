package io.jutil.jdo.internal.core.util;

/**
 * @author Jin Zheng
 * @since 1.0 2022-01-14
 */
public class EnumUtil {
	private EnumUtil() {
	}

	public static <T> T fromString(Class<T> clazz, String name) {
		if (name == null || name.isEmpty()) {
			return null;
		}

		T[] vals = clazz.getEnumConstants();
		if (vals == null || vals.length == 0) {
			return null;
		}
		for (var val : vals) {
			var e = (Enum) val;
			if (e.name().equals(name)) {
				return val;
			}
		}
		return null;
	}

}
