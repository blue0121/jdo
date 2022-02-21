package io.jutil.jdo.internal.core.executor.mapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * @author Jin Zheng
 * @since 2022-02-21
 */
public class DoubleRowMapper implements RowMapper<Double> {
	public DoubleRowMapper() {
	}

	@Override
	public Class<Double> getType() {
		return Double.class;
	}

	@Override
	public Double mapRow(ResultSetMetaData meta, ResultSet rs, int row) throws SQLException {
		if (meta.getColumnCount() < ONE) {
			return null;
		}
		double val = rs.getDouble(ONE);
		return rs.wasNull() ? null : val;
	}
}
