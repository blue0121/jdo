package io.jutil.jdo.core.collection;

import io.jutil.jdo.internal.core.collection.ConcurrentHashSet;

import java.util.Collection;
import java.util.Set;

/**
 * @author Jin Zheng
 * @since 2022-02-17
 */
public interface ConcurrentSet<E> extends Set<E> {

	/**
	 * 创建ConcurrentSet
	 *
	 * @param <E>
	 * @return
	 */
	static <E> ConcurrentSet<E> create() {
		return new ConcurrentHashSet<>();
	}

	/**
	 * 创建 ConcurrentSet
	 *
	 * @param c
	 * @param <E>
	 * @return
	 */
	static <E> ConcurrentSet<E> create(Collection<E> c) {
		if (c == null || c.isEmpty()) {
			return new ConcurrentHashSet<>();
		}

		return new ConcurrentHashSet<>(c);
	}

}
