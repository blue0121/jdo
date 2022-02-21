package io.jutil.jdo.internal.core.executor.mapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * @author Jin Zheng
 * @since 2022-02-21
 */
public class FloatRowMapper implements RowMapper<Float> {
	public FloatRowMapper() {
	}

	@Override
	public Class<Float> getType() {
		return Float.class;
	}

	@Override
	public Float mapRow(ResultSetMetaData meta, ResultSet rs, int row) throws SQLException {
		if (meta.getColumnCount() < ONE) {
			return null;
		}
		float val = rs.getFloat(ONE);
		return rs.wasNull() ? null : val;
	}
}
