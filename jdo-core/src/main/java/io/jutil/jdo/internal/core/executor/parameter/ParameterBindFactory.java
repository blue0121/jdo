package io.jutil.jdo.internal.core.executor.parameter;

/**
 * @author Jin Zheng
 * @since 2022-03-13
 */
public interface ParameterBindFactory<T> {
	/**
	 * 获取参数绑定器
	 *
	 * @param clazz
	 * @return
	 */
	ParameterBinder<T> getBinder(Class<T> clazz);
}
