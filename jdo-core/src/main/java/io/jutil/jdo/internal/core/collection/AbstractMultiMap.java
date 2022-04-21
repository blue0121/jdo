package io.jutil.jdo.internal.core.collection;

import io.jutil.jdo.core.collection.MultiMap;

import java.util.Map;
import java.util.Set;

/**
 * @author Jin Zheng
 * @since 2022-02-17
 */
public abstract class AbstractMultiMap<K, V> implements MultiMap<K, V> {
	protected Map<K, Set<V>> map;

	protected AbstractMultiMap() {
	}

	@Override
	public boolean containsKey(K key) {
		return map.containsKey(key);
	}

	@Override
	public Set<Map.Entry<K, Set<V>>> entrySet() {
		return map.entrySet();
	}

	@Override
	public Set<V> get(K key) {
		Set<V> set = map.get(key);
		if (set == null || set.isEmpty()) {
			return Set.of();
		}

		return Set.copyOf(set);
	}

	@Override
	public V getOne(K key) {
		Set<V> set = map.get(key);
		if (set == null || set.isEmpty()) {
			return null;
		}

		V value = set.iterator().next();
		return value;
	}

	@Override
	public boolean isEmpty() {
		return map.isEmpty();
	}

	@Override
	public int size() {
		return map.size();
	}

}
