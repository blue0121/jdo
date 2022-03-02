package io.jutil.jdo.internal.core.path;

import java.io.File;
import java.io.FileFilter;

/**
 * 类处理器
 *
 * @author Jin Zheng
 * @since 2022-03-02
 */
public interface ClassHandler extends FileFilter {
	String SUFFIX_CLASS = ".class";

	/**
	 * 处理类
	 *
	 * @param clazz 类型
	 */
	void handle(Class<?> clazz);

	/**
	 * 类过滤器默认实现
	 *
	 * @param path
	 * @return
	 */
	@Override
	default boolean accept(File path) {
		if (path.isDirectory()) {
			return true;
		}

		return path.isFile() && path.getName().endsWith(SUFFIX_CLASS);
	}
}
