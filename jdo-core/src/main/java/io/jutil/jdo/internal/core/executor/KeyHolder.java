package io.jutil.jdo.internal.core.executor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author Jin Zheng
 * @since 2022-02-18
 */
public interface KeyHolder {

	/**
	 * 获取数值主键
	 *
	 * @return
	 */
	Number getKey();

	/**
	 * 获取数值主键列表
	 *
	 * @return
	 */
	List<Number> getKeyList();

	/**
	 * 从ResultSet获取数值主键
	 *
	 * @param rs
	 */
	void mapRow(ResultSet rs) throws SQLException;

}
