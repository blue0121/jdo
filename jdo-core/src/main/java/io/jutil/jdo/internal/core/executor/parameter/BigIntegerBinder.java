package io.jutil.jdo.internal.core.executor.parameter;

import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.SQLException;

/**
 * @author Jin Zheng
 * @since 2022-03-09
 */
@NoArgsConstructor
public class BigIntegerBinder implements ParameterBinder<BigInteger> {


    @Override
    public Class<BigInteger> getType() {
        return BigInteger.class;
    }

	@Override
	public void bind(BindContext<BigInteger> context) throws SQLException {
		var pstmt = context.getPreparedStatement();
		var i = context.getIndex();
		var value = context.getValue();
		pstmt.setBigDecimal(i, new BigDecimal(value));
	}

	@Override
	public BigInteger fetch(FetchContext context) throws SQLException {
		var rs = context.getResultSet();
		var i = context.getIndex();
		var n = rs.getBigDecimal(i);
		return n == null ? null : n.toBigInteger();
	}

}
