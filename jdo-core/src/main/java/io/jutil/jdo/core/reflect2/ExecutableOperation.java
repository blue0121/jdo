package io.jutil.jdo.core.reflect2;

import java.util.List;

/**
 * 可执行操作
 *
 * @author Jin Zheng
 * @since 2022-02-17
 */
public interface ExecutableOperation extends AnnotationOperation, NameOperation {

	/**
	 * 参数类型
	 *
	 * @return
	 */
	List<Class<?>> getParameterTypeList();

	/**
	 * 方法参数列表
	 *
	 * @return
	 */
	List<ParameterOperation> getParameterList();

	/**
	 * 可见性
	 *
	 * @return
	 */
	int getModifiers();

}
