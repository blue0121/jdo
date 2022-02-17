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
	 * SQL: select * from [table] where id=?
	 */
	SqlItem getSelectById();

	/**
	 * SQL: select * from [table] where id in (?,?,?)
	 */
	SqlItem getSelectByIdList();

	/**
	 * SQL: insert into [table] (id,x) values (?,?)
	 */
	SqlItem getInsert();

	/**
	 * SQL: update [table] set x=? where id=?
	 */
	SqlItem getUpdateById();

	/**
	 * SQL: update [table] set x=? where id=? and version=?
	 */
	SqlItem getUpdateByIdAndVersion();

	/**
	 * SQL: delete from [table] where id=?
	 */
	SqlItem getDeleteById();

	/**
	 * SQL: delete from [table] where id in (?,?,?)
	 */
	SqlItem getDeleteByIdList();

}
