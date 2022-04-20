package io.jutil.jdo.internal.core.executor.parameter;

import lombok.NoArgsConstructor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * @author Jin Zheng
 * @since 2022-03-09
 */
@NoArgsConstructor
public class IntegerBinder implements ParameterBinder<Integer> {


	@Override
	public Class<Integer> getType() {
		return Integer.class;
	}

	@Override
	public void bind(PreparedStatement pstmt, int i, Integer val) throws SQLException {
		pstmt.setInt(i, val);
	}

	@Override
	public Integer fetch(ResultSetMetaData rsmd, ResultSet rs, int i) throws SQLException {
		var n = rs.getInt(i);
		return rs.wasNull() ? null : n;
	}
}
