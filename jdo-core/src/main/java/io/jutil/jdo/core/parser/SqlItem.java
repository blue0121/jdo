package io.jutil.jdo.core.parser;

import java.util.List;

/**
 * SQL配置项
 *
 * @author Jin Zheng
 * @since 2022-02-16
 */
public interface SqlItem {
	/**
	 * 检查验证配置
	 */
	void check();

	/**
	 * sql
	 */
	String getSql();

	/**
	 * SQL参数占位符(?)
	 */
	List<String> getParamNameList();

}
