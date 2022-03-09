package io.jutil.jdo.internal.core.executor.parameter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author Jin Zheng
 * @since 2022-03-09
 */
public class BingIntegerBinder implements ParameterBinder<BigInteger> {
	public BingIntegerBinder() {
	}

    @Override
    public Class<BigInteger> getType() {
        return BigInteger.class;
    }

    @Override
    public void bind(PreparedStatement pstmt, int i, BigInteger val) throws SQLException {
		pstmt.setBigDecimal(i, new BigDecimal(val));
    }
}
