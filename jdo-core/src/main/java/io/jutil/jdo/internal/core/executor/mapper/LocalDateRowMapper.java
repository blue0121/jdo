package io.jutil.jdo.internal.core.executor.mapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.LocalDate;

/**
 * @author Jin Zheng
 * @since 2022-02-21
 */
public class LocalDateRowMapper implements RowMapper<LocalDate> {
	public LocalDateRowMapper() {
	}

	@Override
	public Class<LocalDate> getType() {
		return LocalDate.class;
	}

	@Override
	public LocalDate mapRow(ResultSetMetaData meta, ResultSet rs, int row) throws SQLException {
		if (meta.getColumnCount() < ONE) {
			return null;
		}
		var date = rs.getDate(ONE);
		return date == null ? null : date.toLocalDate();
	}
}
