package io.jutil.jdo.internal.core.executor.mapper;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * @author Jin Zheng
 * @since 2022-02-21
 */
public class BigDecimalRowMapper implements RowMapper<BigDecimal> {
	public BigDecimalRowMapper() {
	}

	@Override
	public Class<BigDecimal> getType() {
		return BigDecimal.class;
	}

	@Override
	public BigDecimal mapRow(ResultSetMetaData meta, ResultSet rs, int row) throws SQLException {
		if (meta.getColumnCount() < ONE) {
			return null;
		}
		return rs.getBigDecimal(ONE);
	}
}
