package io.jutil.jdo.core.parser;

import io.jutil.jdo.core.reflect.JavaBean;

import java.util.Map;

/**
 * 非实体配置
 *
 * @author Jin Zheng
 * @since 2022-02-16
 */
public interface MapperConfig {
	/**
	 * 配置类型
	 */
	ConfigType getConfigType();

	/**
	 * 检查验证配置
	 */
	void check();

	/**
	 * 对象类型
	 */
	Class<?> getClazz();

	/**
	 * JavaBean
	 */
	JavaBean getJavaBean();

	/**
	 * 所有普通字段配置
	 *
	 * @return Map<字段名, 普通字段配置>
	 */
	Map<String, ColumnConfig> getColumnMap();

}
