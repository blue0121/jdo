package io.jutil.jdo.core.annotation;

/**
 * 锁类型
 *
 * @author Jin Zheng
 * @since 2022-02-16
 */
public enum LockModeType {
	/**
	 * 读锁
	 */
	READ,

	/**
	 * 写锁
	 */
	WRITE,

	/**
	 * 无锁
	 */
	NONE

}
