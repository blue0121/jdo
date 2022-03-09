package io.jutil.jdo.internal.core.executor.parameter;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;

/**
 * @author Jin Zheng
 * @since 2022-03-09
 */
public class SqlTimeBinder implements ParameterBinder<Time> {
	public SqlTimeBinder() {
	}

	@Override
	public Class<Time> getType() {
		return Time.class;
	}

	@Override
	public void bind(PreparedStatement pstmt, int i, Time val) throws SQLException {
		pstmt.setTime(i, val);
	}
}
