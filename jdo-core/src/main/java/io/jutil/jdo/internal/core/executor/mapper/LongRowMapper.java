package io.jutil.jdo.internal.core.executor.mapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * @author Jin Zheng
 * @since 2022-02-21
 */
public class LongRowMapper implements RowMapper<Long> {
	public LongRowMapper() {
	}

	@Override
	public Class<Long> getType() {
		return Long.class;
	}

	@Override
	public Long mapRow(ResultSetMetaData meta, ResultSet rs, int row) throws SQLException {
		if (meta.getColumnCount() < ONE) {
			return null;
		}
		long val = rs.getLong(ONE);
		return rs.wasNull() ? null : val;
	}
}
