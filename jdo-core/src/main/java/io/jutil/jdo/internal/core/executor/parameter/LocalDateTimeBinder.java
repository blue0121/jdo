package io.jutil.jdo.internal.core.executor.parameter;

import lombok.NoArgsConstructor;

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
@NoArgsConstructor
public class LocalDateTimeBinder implements ParameterBinder<LocalDateTime> {


	@Override
	public Class<LocalDateTime> getType() {
		return LocalDateTime.class;
	}

	@Override
	public void bind(BindContext<LocalDateTime> context) throws SQLException {
		var pstmt = context.getPreparedStatement();
		var i = context.getIndex();
		var value = context.getValue();
		pstmt.setTimestamp(i, Timestamp.valueOf(value));
	}

	@Override
	public LocalDateTime fetch(FetchContext context) throws SQLException {
		var rs = context.getResultSet();
		var i = context.getIndex();
		var d = rs.getTimestamp(i);
		return rs.wasNull() ? null : d.toLocalDateTime();
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
