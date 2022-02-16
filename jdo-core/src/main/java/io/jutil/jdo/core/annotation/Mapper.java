package io.jutil.jdo.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 非实体数据查询结果映射
 *
 * @author Jin Zheng
 * @since 2022-02-16
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Mapper {
	/**
	 * 映射字段名，默认映射为大小写字段名<br/>
	 * user_id => userId
	 */
	String name() default "";
}
