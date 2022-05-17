package io.jutil.jdo.core.engine;

/**
 * 事务管理器
 *
 * @author Jin Zheng
 * @since 2022-05-17
 */
public interface TransactionManager {

	/**
	 * 开始事务
	 */
	void begin();

	/**
	 * 提交事务
	 */
	void commit();

	/**
	 * 回滚事务
	 */
	void rollback();

}
