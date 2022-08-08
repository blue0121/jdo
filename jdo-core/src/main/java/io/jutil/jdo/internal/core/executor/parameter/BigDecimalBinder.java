package io.jutil.jdo.internal.core.executor.parameter;

import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * @author Jin Zheng
 * @since 2022-03-09
 */
@NoArgsConstructor
public class BigDecimalBinder implements ParameterBinder<BigDecimal> {


	@Override
	public Class<BigDecimal> getType() {
		return BigDecimal.class;
	}

	@Override
	public void bind(BindContext<BigDecimal> context) throws SQLException {
		var pstmt = context.getPreparedStatement();
		var i = context.getIndex();
		var value = context.getValue();
		pstmt.setBigDecimal(i, value);
	}

	@Override
	public BigDecimal fetch(FetchContext context) throws SQLException {
		var rs = context.getResultSet();
		var i = context.getIndex();
		return rs.getBigDecimal(i);
	}

	@Override
	public void bind(PreparedStatement pstmt, int i, BigDecimal val) throws SQLException {
		pstmt.setBigDecimal(i, val);
	}

	@Override
	public BigDecimal fetch(ResultSetMetaData rsmd, ResultSet rs, int i) throws SQLException {
		return rs.getBigDecimal(i);
	}
}
