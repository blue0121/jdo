package io.jutil.jdo.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注解字段，当字段为空时，动态生成SQL语句也要把空写进数据库
 *
 * @author Jin Zheng
 * @since 2022-02-16
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Must {
	/**
	 * 动态生成insert语句，要把字段的空值强制插入数据库
	 */
	boolean insert() default false;

	/**
	 * 动态生成update语句，要把字段的空值强制更新数据库
	 */
	boolean update() default true;
}
