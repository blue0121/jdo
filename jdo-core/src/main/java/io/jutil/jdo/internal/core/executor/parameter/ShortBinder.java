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
public class ShortBinder implements ParameterBinder<Short> {


	@Override
	public Class<Short> getType() {
		return Short.class;
	}

	@Override
	public void bind(PreparedStatement pstmt, int i, Short val) throws SQLException {
		pstmt.setShort(i, val);
	}

	@Override
	public Short fetch(ResultSetMetaData rsmd, ResultSet rs, int i) throws SQLException {
		var n = rs.getShort(i);
		return rs.wasNull() ? null : n;
	}
}
