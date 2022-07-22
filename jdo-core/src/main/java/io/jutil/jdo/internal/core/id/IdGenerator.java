package io.jutil.jdo.internal.core.id;

/**
 * @author Jin Zheng
 * @since 2022-05-26
 */
public interface IdGenerator<T> {

	/**
	 * 生成主键
	 *
	 * @return
	 */
	T generate();

}
