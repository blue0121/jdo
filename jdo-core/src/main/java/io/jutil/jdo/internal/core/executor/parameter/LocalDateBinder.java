package io.jutil.jdo.internal.core.executor.parameter;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.LocalDate;

/**
 * @author Jin Zheng
 * @since 2022-03-09
 */
public class LocalDateBinder implements ParameterBinder<LocalDate> {
	public LocalDateBinder() {
	}

	@Override
	public Class<LocalDate> getType() {
		return LocalDate.class;
	}

	@Override
	public void bind(PreparedStatement pstmt, int i, LocalDate val) throws SQLException {
		pstmt.setDate(i, Date.valueOf(val));
	}

	@Override
	public LocalDate fetch(ResultSetMetaData rsmd, ResultSet rs, int i) throws SQLException {
		var d = rs.getDate(i);
		return rs.wasNull() ? null : d.toLocalDate();
	}
}
