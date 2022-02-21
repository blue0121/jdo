package io.jutil.jdo.internal.core.executor.mapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * @author Jin Zheng
 * @since 2022-02-21
 */
public class SqlDateRowMapper implements RowMapper<Date> {
	public SqlDateRowMapper() {
	}

	@Override
	public Class<Date> getType() {
		return Date.class;
	}

	@Override
	public Date mapRow(ResultSetMetaData meta, ResultSet rs, int row) throws SQLException {
		if (meta.getColumnCount() < ONE) {
			return null;
		}
		return rs.getDate(ONE);
	}
}
