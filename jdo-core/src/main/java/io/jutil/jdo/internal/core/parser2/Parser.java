package io.jutil.jdo.internal.core.parser2;

import io.jutil.jdo.core.parser2.MapperMetadata;

/**
 * 解析
 *
 * @author Jin Zheng
 * @since 2022-05-11
 */
public interface Parser {

	/**
	 * 解析 @Entity/@Mapper 注解
	 *
	 * @param clazz
	 * @return
	 */
	MapperMetadata parse(Class<?> clazz);

}
