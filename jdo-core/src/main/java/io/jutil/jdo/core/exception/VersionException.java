package io.jutil.jdo.core.exception;

/**
 * @author Jin Zheng
 * @since 2022-02-16
 */
public class VersionException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	private Class<?> clazz;

	public VersionException(Class<?> clazz) {
		super(clazz.getName() + " 版本冲突");
		this.clazz = clazz;
	}

	public Class<?> getClazz() {
		return clazz;
	}

}
