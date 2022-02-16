package io.jutil.jdo.core.parser;

import blue.base.core.reflect.JavaBean;

import java.util.Map;

/**
 * 实体配置
 *
 * @author Jin Zheng
 * @since 2022-02-16
 */
public interface EntityConfig {

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
	 * 表名
	 */
	String getTableName();

	/**
	 * 转义后的表名
	 */
	String getEscapeTableName();

	/**
	 * 单个主键配置
	 */
	IdConfig getIdConfig();

	/**
	 * 所有主键配置
	 *
	 * @return Map<字段名, 主键配置>
	 */
	Map<String, IdConfig> getIdMap();

	/**
	 * 版本配置
	 */
	VersionConfig getVersionConfig();

	/**
	 * 所有普通字段配置
	 *
	 * @return Map<字段名, 普通字段配置>
	 */
	Map<String, ColumnConfig> getColumnMap();

	/**
	 * 额外字段配置
	 *
	 * @return Map<字段名, 额外字段配置>
	 */
	Map<String, ColumnConfig> getExtraMap();

	/**
	 * SQL配置
	 */
	SqlConfig getSqlConfig();

}
