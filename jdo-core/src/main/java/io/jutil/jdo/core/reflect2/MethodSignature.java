package io.jutil.jdo.core.reflect2;

import io.jutil.jdo.core.reflect.NameOperation;

/**
 * 方法签名
 *
 * @author Jin Zheng
 * @since 2022-02-17
 */
public interface MethodSignature extends NameOperation {

	/**
	 * 方法参数类型
	 *
	 * @return
	 */
	Class<?>[] getParameterTypes();

}
