package io.jutil.jdo.internal.core.executor.parameter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * @author Jin Zheng
 * @since 2022-03-09
 */
public class StringBinder implements ParameterBinder<String> {
	public StringBinder() {
	}

	@Override
	public Class<String> getType() {
		return String.class;
	}

	@Override
	public void bind(PreparedStatement pstmt, int i, String val) throws SQLException {
		pstmt.setString(i, val);
	}

	@Override
	public String fetch(ResultSetMetaData rsmd, ResultSet rs, int i) throws SQLException {
		return rs.getString(i);
	}
}
