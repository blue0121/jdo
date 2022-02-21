package io.jutil.jdo.internal.core.executor.mapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * @author Jin Zheng
 * @since 2022-02-21
 */
public class SqlTimestampRowMapper implements RowMapper<Timestamp> {
	public SqlTimestampRowMapper() {
	}

	@Override
	public Class<Timestamp> getType() {
		return Timestamp.class;
	}

	@Override
	public Timestamp mapRow(ResultSetMetaData meta, ResultSet rs, int row) throws SQLException {
		if (meta.getColumnCount() < ONE) {
			return null;
		}
		return rs.getTimestamp(ONE);
	}
}
