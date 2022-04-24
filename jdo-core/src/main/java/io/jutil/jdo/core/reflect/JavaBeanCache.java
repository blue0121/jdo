package io.jutil.jdo.core.reflect;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author Jin Zheng
 * @since 2022-04-24
 */
public class JavaBeanCache {
	private static ConcurrentMap<Class<?>, JavaBean> javaBeanMap = new ConcurrentHashMap<>();

	private JavaBeanCache() {
	}

	/**
	 * 根据目标类型获取缓存中的JavaBean对象，不存在则创建
	 *
	 * @param targetClass
	 * @return
	 */
	public static JavaBean get(Class<?> targetClass) {
		return javaBeanMap.computeIfAbsent(targetClass, k -> JavaBean.create(k));
	}

}
