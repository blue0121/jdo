package io.jutil.jdo.internal.core.sql;

/**
 * @author Jin Zheng
 * @since 2022-03-21
 */
public interface SqlHandler {

	void handle(SqlRequest request, SqlResponse response);

}
