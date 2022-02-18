package io.jutil.jdo.internal.core.parser;

import io.jutil.jdo.core.reflect.JavaBean;

/**
 * @author Jin Zheng
 * @since 2022-02-18
 */
public interface Parser {

	/**
	 * 解析 @Entity/@Mapper 注解
	 *
	 * @param bean
	 */
	void parse(JavaBean bean);
}
