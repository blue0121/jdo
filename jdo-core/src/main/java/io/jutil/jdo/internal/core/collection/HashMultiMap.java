package io.jutil.jdo.internal.core.collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Jin Zheng
 * @since 2022-02-17
 */
public class HashMultiMap<K, V> extends AbstractMultiMap<K, V> {
	private static Logger logger = LoggerFactory.getLogger(HashMultiMap.class);
	private Class<?> mapType;

	public HashMultiMap(Map<K, Set<V>> map) {
		this.map = map;
		this.mapType = map.getClass();
		if (logger.isDebugEnabled()) {
			logger.debug("使用 {}", map.getClass().getSimpleName());
		}
	}

	@Override
	public void clear() {
		map.clear();
	}

	@Override
	public V put(K key, V value) {
		Set<V> set = null;
		if (map instanceof LinkedHashMap) {
			set = map.computeIfAbsent(key, k -> new LinkedHashSet<>());
		} else if (map instanceof HashMap) {
			set = map.computeIfAbsent(key, k -> new HashSet<>());
		} else {
			set = map.computeIfAbsent(key, k -> new ConcurrentHashSet<>());
		}
		set.add(value);
		return value;
	}

	@Override
	public boolean remove(K key) {
		Set<V> set = map.remove(key);
		return set != null;
	}

	@Override
	public boolean remove(K key, V value) {
		RemoveResult result = new RemoveResult();
		map.computeIfPresent(key, (k, set) -> {
			result.result = set.remove(value);
			return set.isEmpty() ? null : set;
		});
		return result.result;
	}

	@Override
	public Class<?> getMapType() {
		return mapType;
	}

	private class RemoveResult {
		public boolean result = false;
	}
}
