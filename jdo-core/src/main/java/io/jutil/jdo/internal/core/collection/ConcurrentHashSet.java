package io.jutil.jdo.internal.core.collection;

import io.jutil.jdo.core.collection.ConcurrentSet;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author Jin Zheng
 * @since 2022-02-17
 */
public class ConcurrentHashSet<E> extends AbstractSet<E> implements ConcurrentSet<E> {
	private static final Object PRESENT = new Object();

	private ConcurrentMap<E, Object> map;

	public ConcurrentHashSet() {
		map = new ConcurrentHashMap<>();
	}

	public ConcurrentHashSet(Collection<? extends E> c) {
		map = new ConcurrentHashMap<>(Math.max((int) (c.size() / .75f) + 1, 16));
		addAll(c);
	}

	public ConcurrentHashSet(int initialCapacity, float loadFactor) {
		map = new ConcurrentHashMap<>(initialCapacity, loadFactor);
	}

	public ConcurrentHashSet(int initialCapacity) {
		map = new ConcurrentHashMap<>(initialCapacity);
	}

	@Override
	public Iterator<E> iterator() {
		return map.keySet().iterator();
	}

	@Override
	public int size() {
		return map.size();
	}

	@Override
	public boolean isEmpty() {
		return map.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return map.containsKey(o);
	}

	@Override
	public boolean add(E e) {
		return map.put(e, PRESENT) == null;
	}

	@Override
	public boolean remove(Object o) {
		return map.remove(o) == PRESENT;
	}

	@Override
	public void clear() {
		map.clear();
	}

}
