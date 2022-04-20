package io.jutil.jdo.internal.core.executor.parameter;

import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * @author Jin Zheng
 * @since 2022-03-09
 */
@NoArgsConstructor
public class BingIntegerBinder implements ParameterBinder<BigInteger> {


    @Override
    public Class<BigInteger> getType() {
        return BigInteger.class;
    }

    @Override
    public void bind(PreparedStatement pstmt, int i, BigInteger val) throws SQLException {
		pstmt.setBigDecimal(i, new BigDecimal(val));
    }

	@Override
	public BigInteger fetch(ResultSetMetaData rsmd, ResultSet rs, int i) throws SQLException {
		var bd = rs.getBigDecimal(i);
		if (bd == null) {
			return null;
		}
		return bd.toBigInteger();
	}

}
