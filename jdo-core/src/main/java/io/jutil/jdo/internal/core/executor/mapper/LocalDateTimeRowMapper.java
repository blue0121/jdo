package io.jutil.jdo.internal.core.executor.mapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * @author Jin Zheng
 * @since 2022-02-21
 */
public class LocalDateTimeRowMapper implements RowMapper<LocalDateTime> {
	public LocalDateTimeRowMapper() {
	}

	@Override
	public Class<LocalDateTime> getType() {
		return LocalDateTime.class;
	}

	@Override
	public LocalDateTime mapRow(ResultSetMetaData meta, ResultSet rs, int row) throws SQLException {
		if (meta.getColumnCount() < ONE) {
			return null;
		}
		var ts = rs.getTimestamp(ONE);
		return ts == null ? null : ts.toLocalDateTime();
	}
}
