package io.jutil.jdo.internal.core.util;

/**
 * @author Jin Zheng
 * @since 2022-02-18
 */
public class ObjectUtil {
	private static final String EMPTY_STR = "";

	private ObjectUtil() {
	}

	public static boolean isEmpty(Object object) {
		return object == null || EMPTY_STR.equals(object);
	}

}
