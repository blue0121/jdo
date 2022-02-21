package io.jutil.jdo.internal.core.executor.mapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.LocalTime;

/**
 * @author Jin Zheng
 * @since 2022-02-21
 */
public class LocalTimeRowMapper implements RowMapper<LocalTime> {
	public LocalTimeRowMapper() {
	}

	@Override
	public Class<LocalTime> getType() {
		return LocalTime.class;
	}

	@Override
	public LocalTime mapRow(ResultSetMetaData meta, ResultSet rs, int row) throws SQLException {
		if (meta.getColumnCount() < ONE) {
			return null;
		}
		var time = rs.getTime(ONE);
		return time == null ? null : time.toLocalTime();
	}
}
