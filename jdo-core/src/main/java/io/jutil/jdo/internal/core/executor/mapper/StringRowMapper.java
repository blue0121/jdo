package io.jutil.jdo.internal.core.executor.mapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * @author Jin Zheng
 * @since 2022-02-21
 */
public class StringRowMapper implements RowMapper<String> {
	public StringRowMapper() {
	}

	@Override
	public Class<String> getType() {
		return String.class;
	}

	@Override
	public String mapRow(ResultSetMetaData meta, ResultSet rs, int row) throws SQLException {
		if (meta.getColumnCount() < ONE) {
			return null;
		}
		return rs.getString(ONE);
	}
}
