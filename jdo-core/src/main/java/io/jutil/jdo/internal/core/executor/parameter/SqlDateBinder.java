package io.jutil.jdo.internal.core.executor.parameter;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author Jin Zheng
 * @since 2022-03-09
 */
public class SqlDateBinder implements ParameterBinder<Date> {
	public SqlDateBinder() {
	}

	@Override
	public Class<Date> getType() {
		return Date.class;
	}

	@Override
	public void bind(PreparedStatement pstmt, int i, Date val) throws SQLException {
		pstmt.setDate(i, val);
	}
}
