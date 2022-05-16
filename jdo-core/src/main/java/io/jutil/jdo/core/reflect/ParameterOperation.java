package io.jutil.jdo.core.reflect;

/**
 * 参数操作
 *
 * @author Jin Zheng
 * @since 2022-04-28
 */
public interface ParameterOperation extends AnnotationOperation, NameOperation {

	/**
	 * 参数类型
	 *
	 * @return
	 */
	Class<?> getType();

}
