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
public class FloatBinder implements ParameterBinder<Float> {


	@Override
	public Class<Float> getType() {
		return Float.class;
	}

	@Override
	public void bind(PreparedStatement pstmt, int i, Float val) throws SQLException {
		pstmt.setFloat(i, val);
	}

	@Override
	public Float fetch(ResultSetMetaData rsmd, ResultSet rs, int i) throws SQLException {
		var n = rs.getFloat(i);
		return rs.wasNull() ? null : n;
	}
}
