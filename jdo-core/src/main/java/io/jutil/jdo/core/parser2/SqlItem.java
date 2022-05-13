package io.jutil.jdo.core.parser2;

import java.util.List;

/**
 * SQL元数据项
 *
 * @author Jin Zheng
 * @since 2022-05-11
 */
public interface SqlItem {

	/**
	 * SQL语句
	 *
	 * @return
	 */
	String getSql();

	/**
	 * SQL参数占位符名称列表
	 *
	 * @return
	 */
	List<String> getParameterNameList();
}
