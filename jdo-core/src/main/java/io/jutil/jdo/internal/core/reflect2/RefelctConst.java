package io.jutil.jdo.internal.core.reflect2;

import java.util.Set;

/**
 * @author Jin Zheng
 * @since 2022-05-01
 */
public class RefelctConst {

	public static final Set<String> IGNORE_METHOD_SET = Set.of("wait", "equals", "toString", "hashCode", "getClass",
			"notify", "notifyAll");

	private RefelctConst() {
	}
}
