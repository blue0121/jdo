package io.jutil.jdo.internal.core.reflect;


import io.jutil.jdo.internal.core.util.AssertUtil;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author Jin Zheng
 * @since 2022-02-17
 */
public class ParameterTypeKey {
	private final Class<?>[] types;

	public ParameterTypeKey(Class<?>[] types) {
		AssertUtil.notNull(types, "类型");
		this.types = types;
	}

	@SuppressWarnings("SuspiciousToArrayCall")
	public ParameterTypeKey(Collection<?> types) {
		AssertUtil.notNull(types, "类型");
		this.types = types.toArray(new Class[0]);
	}

	public Class<?>[] getTypes() {
		return types;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		ParameterTypeKey that = (ParameterTypeKey) o;
		return Arrays.equals(types, that.types);
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(types);
	}
}
