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
public class DoubleBinder implements ParameterBinder<Double> {


	@Override
	public Class<Double> getType() {
		return Double.class;
	}

	@Override
	public void bind(PreparedStatement pstmt, int i, Double val) throws SQLException {
		pstmt.setDouble(i, val);
	}

	@Override
	public Double fetch(ResultSetMetaData rsmd, ResultSet rs, int i) throws SQLException {
		var n = rs.getDouble(i);
		return rs.wasNull() ? null : n;
	}
}
