package io.jutil.jdo.core.collection;

import io.jutil.jdo.internal.core.collection.HashMultiMap;
import io.jutil.jdo.internal.core.collection.ImmutableMultiMap;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Jin Zheng
 * @since 2022-02-17
 */
public interface MultiMap<K, V> {
	/**
	 * 创建 HashMap
	 *
	 * @param <K>
	 * @param <V>
	 * @return
	 */
	static <K, V> MultiMap<K, V> create() {
		return new HashMultiMap<>(new HashMap<>());
	}

	/**
	 * 创建 LinkedHashMap
	 *
	 * @param <K>
	 * @param <V>
	 * @return
	 */
	static <K, V> MultiMap<K, V> createLinked() {
		return new HashMultiMap<>(new LinkedHashMap<>());
	}

	/**
	 * 创建 ConcurrentHashMap
	 *
	 * @param <K>
	 * @param <V>
	 * @return
	 */
	static <K, V> MultiMap<K, V> createConcurrent() {
		return new HashMultiMap<>(new ConcurrentHashMap<>());
	}

	/**
	 * 清理
	 */
	void clear();

	/**
	 * 是否存在键
	 *
	 * @param key
	 * @return
	 */
	boolean containsKey(K key);

	/**
	 * 键/值对
	 *
	 * @return
	 */
	Set<Map.Entry<K, Set<V>>> entrySet();

	/**
	 * 根据键获取值列表
	 *
	 * @param key
	 * @return
	 */
	Set<V> get(K key);

	/**
	 * 根据键获取单个值，不存在返回空
	 *
	 * @param key
	 * @return
	 */
	V getOne(K key);

	/**
	 * 是否为空
	 *
	 * @return
	 */
	boolean isEmpty();

	/**
	 * 添加/更新键值对
	 *
	 * @param key
	 * @param value
	 * @return
	 */
	V put(K key, V value);

	/**
	 * 根据键删除
	 *
	 * @param key
	 * @return
	 */
	boolean remove(K key);

	/**
	 * 根据键值对删除
	 *
	 * @param key
	 * @param value
	 * @return
	 */
	boolean remove(K key, V value);

	/**
	 * 个数
	 *
	 * @return
	 */
	int size();

	/**
	 * represent map class
	 *
	 * @return
	 */
	Class<?> getMapType();

	/**
	 * 复制只读MultiMap
	 *
	 * @param map
	 * @param <K>
	 * @param <V>
	 * @return
	 */
	static <K, V> MultiMap<K, V> copyOf(MultiMap<K, V> map) {
		return new ImmutableMultiMap<>(map);
	}

}
