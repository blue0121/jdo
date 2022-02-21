package io.jutil.jdo.core.engine;

/**
 * @author Jin Zheng
 * @since 2022-02-21
 */
public interface OrderBy {
	/**
	 * 增加排序SQL: id desc
	 *
	 * @param sql
	 */
	void add(String sql);

}
