package io.jutil.jdo.core.annotation;

/**
 * 主键产生类型
 *
 * @author Jin Zheng
 * @since 2022-02-16
 */
public enum GeneratorType {
	/**
	 * <p>自动判断<p>
	 * <ol>
	 * <li>字段是字符串，用UUID方式</li>
	 * <li>字段是整型，用INCREMENT</li>
	 * <ol>
	 */
	AUTO,

	/**
	 * UUID 方式
	 */
	UUID,

	/**
	 * 自增长
	 */
	INCREMENT,

	/**
	 * 自行分配
	 */
	ASSIGNED
}
