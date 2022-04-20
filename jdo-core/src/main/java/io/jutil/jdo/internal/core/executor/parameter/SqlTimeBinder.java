package io.jutil.jdo.internal.core.executor.parameter;

import lombok.NoArgsConstructor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Time;

/**
 * @author Jin Zheng
 * @since 2022-03-09
 */
@NoArgsConstructor
public class SqlTimeBinder implements ParameterBinder<Time> {


	@Override
	public Class<Time> getType() {
		return Time.class;
	}

	@Override
	public void bind(PreparedStatement pstmt, int i, Time val) throws SQLException {
		pstmt.setTime(i, val);
	}

	@Override
	public Time fetch(ResultSetMetaData rsmd, ResultSet rs, int i) throws SQLException {
		return rs.getTime(i);
	}
}
