package io.jutil.jdo.internal.core.executor.parameter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Jin Zheng
 * @since 2022-03-09
 */
public class ShortBinder implements ParameterBinder<Short> {
	public ShortBinder() {
	}

	@Override
	public Class<Short> getType() {
		return Short.class;
	}

	@Override
	public void bind(PreparedStatement pstmt, int i, Short val) throws SQLException {
		pstmt.setShort(i, val);
	}

	@Override
	public Short fetch(ResultSet rs, int i) throws SQLException {
		var n = rs.getShort(i);
		return rs.wasNull() ? null : n;
	}
}
