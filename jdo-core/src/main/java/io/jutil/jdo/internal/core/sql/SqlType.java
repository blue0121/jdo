package io.jutil.jdo.internal.core.sql;

/**
 * @author Jin Zheng
 * @since 2022-02-18
 */
public enum SqlType {

	/**
	 * 动态 insert SQL语句
	 */
	INSERT,

	/**
	 * 批量 insert SQL 语句
	 */
	BATCH_INSERT,

	/**
	 * 动态 update SQL语句
	 */
	UPDATE,

	/**
	 * 批量 update SQL 语句
	 */
	BATCH_UPDATE,

	/**
	 * 动态 delete SQL 语句
	 */
	DELETE,

	/**
	 * 动态 delete by SQL 语句
	 */
	DELETE_BY,

	/**
	 * 动态增长SQL语句
	 */
	INC,

	/**
	 * 判断是否存在 SQL 语句
	 */
	EXIST,

	/**
	 * 动态 select ... limit 1 单字段 SQL 语句
	 */
	GET_FIELD,

	/**
	 * 动态 select ... limit SQL语句
	 */
	GET,

	/**
	 * 动态 select ... where id SQL语句
	 */
	GET_ID,

	/**
	 * 动态 select count(*) SQL 语句
	 */
	COUNT,

}
