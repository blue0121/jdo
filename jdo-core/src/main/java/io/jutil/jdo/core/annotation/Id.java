package io.jutil.jdo.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 主键注解
 *
 * @author Jin Zheng
 * @since 2022-02-16
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Id {
	/**
	 * 映射字段名，默认映射为大小写字段名<br/>
	 * user_id => userId
	 */
	String name() default "";

	/**
	 * 主键产生方式
	 */
	GeneratorType generator() default GeneratorType.AUTO;
}
