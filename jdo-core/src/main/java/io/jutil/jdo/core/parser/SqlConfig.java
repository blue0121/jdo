package io.jutil.jdo.core.parser;

/**
 * SQL配置
 *
 * @author Jin Zheng
 * @since 2022-02-16
 */
public interface SqlConfig {
	/**
	 * 检查验证配置
	 */
	void check();

	/**
	 * select * from [table] where id=?
	 */
	SqlItem getSelectById();

	/**
	 * select * from [table] where id in (?,?,?)
	 */
	SqlItem getSelectByIdList();

	/**
	 * insert into [table] (id,x) values (?,?)
	 */
	SqlItem getInsert();

	/**
	 * update [table] set x=? where id=?
	 */
	SqlItem getUpdateById();

	/**
	 * update [table] set x=? where id=? and version=?
	 */
	SqlItem getUpdateByIdAndVersion();

	/**
	 * delete from [table] where id=?
	 */
	SqlItem getDeleteById();

	/**
	 * delete from [table] where id in (?,?,?)
	 */
	SqlItem getDeleteByIdList();

}
