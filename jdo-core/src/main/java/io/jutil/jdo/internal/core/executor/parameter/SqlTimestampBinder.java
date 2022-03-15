package io.jutil.jdo.internal.core.executor.parameter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * @author Jin Zheng
 * @since 2022-03-09
 */
public class SqlTimestampBinder implements ParameterBinder<Timestamp> {
	public SqlTimestampBinder() {
	}

	@Override
	public Class<Timestamp> getType() {
		return Timestamp.class;
	}

	@Override
	public void bind(PreparedStatement pstmt, int i, Timestamp val) throws SQLException {
		pstmt.setTimestamp(i, val);
	}

	@Override
	public Timestamp fetch(ResultSetMetaData rsmd, ResultSet rs, int i) throws SQLException {
		return rs.getTimestamp(i);
	}
}
