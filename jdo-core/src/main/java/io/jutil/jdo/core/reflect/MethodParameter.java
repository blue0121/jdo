package io.jutil.jdo.core.reflect;

/**
 * 方法参数
 *
 * @author Jin Zheng
 * @since 2022-02-17
 */
public interface MethodParameter extends AnnotationOperation, NameOperation {

	/**
	 * 参数类型
	 *
	 * @return
	 */
	Class<?> getType();

}
