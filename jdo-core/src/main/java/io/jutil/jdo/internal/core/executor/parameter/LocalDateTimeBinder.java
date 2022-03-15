package io.jutil.jdo.internal.core.executor.parameter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * @author Jin Zheng
 * @since 2022-03-09
 */
public class LocalDateTimeBinder implements ParameterBinder<LocalDateTime> {
	public LocalDateTimeBinder() {
	}

	@Override
	public Class<LocalDateTime> getType() {
		return LocalDateTime.class;
	}

	@Override
	public void bind(PreparedStatement pstmt, int i, LocalDateTime val) throws SQLException {
		pstmt.setTimestamp(i, Timestamp.valueOf(val));
	}

	@Override
	public LocalDateTime fetch(ResultSetMetaData rsmd, ResultSet rs, int i) throws SQLException {
		var d = rs.getTimestamp(i);
		return rs.wasNull() ? null : d.toLocalDateTime();
	}
}
