package io.jutil.jdo.core.engine;

/**
 * @author Jin Zheng
 * @since 2022-02-28
 */
public interface Jdo {

	/**
	 * 获取JdoTemplate
	 */
	JdoTemplate getJdoTemplate();

	/**
	 * 关闭资源，比如数据库链接
	 */
	void close();
}
