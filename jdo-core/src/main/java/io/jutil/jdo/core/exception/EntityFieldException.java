package io.jutil.jdo.core.exception;

/**
 * @author Jin Zheng
 * @since 2022-03-21
 */
public class EntityFieldException extends JdbcException {
	private static final long serialVersionUID = 1L;

	public EntityFieldException(String field) {
		super("字段 [" + field + "] 不存在");
	}

	public EntityFieldException(String field, String message) {
		super("字段 [" + field + "] " + message);
	}

}
