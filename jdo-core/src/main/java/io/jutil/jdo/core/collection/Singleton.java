package io.jutil.jdo.core.collection;

import io.jutil.jdo.internal.core.util.AssertUtil;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

/**
 * @author Jin Zheng
 * @since 2022-02-17
 */
public class Singleton {
	private static final ConcurrentMap<Class<?>, Object> POOL = new ConcurrentHashMap<>();

	private Singleton() {
	}

	@SuppressWarnings("unchecked")
	public static <T> T get(Class<T> clazz) {
		AssertUtil.notNull(clazz, "类型");
		return (T) POOL.computeIfAbsent(clazz, k -> newInstance(k));
	}

	private static <T> T newInstance(Class<T> clazz) {
		try {
			return clazz.getConstructor().newInstance();
		}
		catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T get(Class<T> clazz, Function<Class<T>, T> f) {
		AssertUtil.notNull(clazz, "类型");
		AssertUtil.notNull(f, "回调函数");
		return (T) POOL.computeIfAbsent(clazz, k -> f.apply(clazz));
	}

	public static void put(Object object) {
		AssertUtil.notNull(object, "对象");
		Object old = POOL.putIfAbsent(object.getClass(), object);
		if (old != null && old != object) {
			throw new IllegalArgumentException(object.getClass().getName() + " 已经存在");
		}
	}

	public static void remove(Object object) {
		AssertUtil.notNull(object, "对象");
		if (object instanceof Class) {
			POOL.remove((Class<?>) object);
		}
		else {
			POOL.remove(object.getClass());
		}
	}

	public static void clear() {
		POOL.clear();
	}

	public static boolean isEmpty() {
		return POOL.isEmpty();
	}

	public static int size() {
		return POOL.size();
	}

}
