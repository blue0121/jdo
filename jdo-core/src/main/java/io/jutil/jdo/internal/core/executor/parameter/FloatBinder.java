package io.jutil.jdo.internal.core.executor.parameter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Jin Zheng
 * @since 2022-03-09
 */
public class FloatBinder implements ParameterBinder<Float> {
	public FloatBinder() {
	}

	@Override
	public Class<Float> getType() {
		return Float.class;
	}

	@Override
	public void bind(PreparedStatement pstmt, int i, Float val) throws SQLException {
		pstmt.setFloat(i, val);
	}

	@Override
	public Float fetch(ResultSet rs, int i) throws SQLException {
		var n = rs.getFloat(i);
		return rs.wasNull() ? null : n;
	}
}
