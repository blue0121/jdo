package io.jutil.jdo.core.reflect2;

import io.jutil.jdo.core.reflect.MethodSignature;

/**
 * 方法操作
 *
 * @author Jin Zheng
 * @since 2022-04-28
 */
public interface MethodOperation extends ExecutableOperation {

	/**
	 * 方法签名
	 *
	 * @return
	 */
	MethodSignature getMethodSignature();

	/**
	 * 返回类型
	 *
	 * @return
	 */
	Class<?> getReturnType();

	/**
	 * JavaBean所代表的字段名
	 *
	 * @return
	 */
	String getRepresentFieldName();

	/**
	 * 是否Setter方法
	 *
	 * @return
	 */
	boolean isSetter();

	/**
	 * 是否Getter方法
	 *
	 * @return
	 */
	boolean isGetter();
}
