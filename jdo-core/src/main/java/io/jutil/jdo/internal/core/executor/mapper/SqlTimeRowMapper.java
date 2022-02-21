package io.jutil.jdo.internal.core.executor.mapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Time;

/**
 * @author Jin Zheng
 * @since 2022-02-21
 */
public class SqlTimeRowMapper implements RowMapper<Time> {
	public SqlTimeRowMapper() {
	}

	@Override
	public Class<Time> getType() {
		return Time.class;
	}

	@Override
	public Time mapRow(ResultSetMetaData meta, ResultSet rs, int row) throws SQLException {
		if (meta.getColumnCount() < ONE) {
			return null;
		}
		return rs.getTime(ONE);
	}
}
