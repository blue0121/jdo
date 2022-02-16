package io.jutil.jdo.core.exception;

/**
 * @author Jin Zheng
 * @since 2022-02-16
 */
public class JdbcException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public JdbcException(String message) {
		super(message);
	}

	public JdbcException(Throwable throwable) {
		super(throwable);
	}

}
