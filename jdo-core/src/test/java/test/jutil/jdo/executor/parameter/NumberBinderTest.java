package test.jutil.jdo.executor.parameter;

import io.jutil.jdo.internal.core.sql.SqlParameter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.SQLException;
import java.util.List;

/**
 * @author Jin Zheng
 * @since 2022-03-09
 */
public class NumberBinderTest extends BaseBinderTest {
	public NumberBinderTest() {
	}

	@Test
	public void testBigDecimal() throws SQLException {
		var num = new BigDecimal(ONE);
		facade.bind(pstmt, List.of(SqlParameter.create(num)));
		Mockito.verify(pstmt).setBigDecimal(Mockito.eq(ONE), Mockito.eq(num));

		Mockito.doReturn(num).when(rs).getBigDecimal(Mockito.eq(ONE));
		List<BigDecimal> list = facade.fetch(rs, BigDecimal.class);
		Assertions.assertEquals(ONE, list.size());
		Assertions.assertEquals(num, list.get(ZERO));
	}

	@Test
	public void testBigInteger() throws SQLException {
		var num = new BigInteger("1");
		facade.bind(pstmt, List.of(SqlParameter.create(num)));
		Mockito.verify(pstmt).setBigDecimal(Mockito.eq(ONE), Mockito.eq(new BigDecimal(ONE)));

		Mockito.doReturn(new BigDecimal(ONE)).when(rs).getBigDecimal(Mockito.eq(ONE));
		List<BigInteger> list = facade.fetch(rs, BigInteger.class);
		Assertions.assertEquals(ONE, list.size());
		Assertions.assertEquals(num, list.get(ZERO));
	}

	@Test
	public void testByteArray() throws SQLException {
		var num = new byte[] {0x1, 0x2};
		facade.bind(pstmt, List.of(SqlParameter.create(num)));
		Mockito.verify(pstmt).setBytes(Mockito.eq(ONE), Mockito.eq(num));

		Mockito.doReturn(num).when(rs).getBytes(Mockito.eq(ONE));
		List<byte[]> list = facade.fetch(rs, byte[].class);
		Assertions.assertEquals(ONE, list.size());
		Assertions.assertEquals(num, list.get(ZERO));
	}

	@Test
	public void testByte() throws SQLException {
		var num = (byte)1;
		facade.bind(pstmt, List.of(SqlParameter.create(num)));
		Mockito.verify(pstmt).setByte(Mockito.eq(ONE), Mockito.eq(num));

		Mockito.doReturn(num).when(rs).getByte(Mockito.eq(ONE));
		List<Byte> list = facade.fetch(rs, Byte.class);
		Assertions.assertEquals(ONE, list.size());
		Assertions.assertEquals(num, list.get(ZERO));
	}

	@Test
	public void testDouble() throws SQLException {
		var num = 1.0d;
		facade.bind(pstmt, List.of(SqlParameter.create(num)));
		Mockito.verify(pstmt).setDouble(Mockito.eq(ONE), Mockito.eq(num));

		Mockito.doReturn(num).when(rs).getDouble(Mockito.eq(ONE));
		List<Double> list = facade.fetch(rs, Double.class);
		Assertions.assertEquals(ONE, list.size());
		Assertions.assertEquals(num, list.get(ZERO));
	}

	@Test
	public void testFloat() throws SQLException {
		var num = 1.0f;
		facade.bind(pstmt, List.of(SqlParameter.create(num)));
		Mockito.verify(pstmt).setFloat(Mockito.eq(ONE), Mockito.eq(num));

		Mockito.doReturn(num).when(rs).getFloat(Mockito.eq(ONE));
		List<Float> list = facade.fetch(rs, Float.class);
		Assertions.assertEquals(ONE, list.size());
		Assertions.assertEquals(num, list.get(ZERO));
	}

	@Test
	public void testInteger() throws SQLException {
		var num = 1;
		facade.bind(pstmt, List.of(SqlParameter.create(num)));
		Mockito.verify(pstmt).setInt(Mockito.eq(ONE), Mockito.eq(num));

		Mockito.doReturn(num).when(rs).getInt(Mockito.eq(ONE));
		List<Integer> list = facade.fetch(rs, Integer.class);
		Assertions.assertEquals(ONE, list.size());
		Assertions.assertEquals(num, list.get(ZERO));
	}

	@Test
	public void testLong() throws SQLException {
		var num = 1L;
		facade.bind(pstmt, List.of(SqlParameter.create(num)));
		Mockito.verify(pstmt).setLong(Mockito.eq(ONE), Mockito.eq(num));

		Mockito.doReturn(num).when(rs).getLong(Mockito.eq(ONE));
		List<Long> list = facade.fetch(rs, Long.class);
		Assertions.assertEquals(ONE, list.size());
		Assertions.assertEquals(num, list.get(ZERO));
	}

	@Test
	public void testShort() throws SQLException {
		var num = (short)1;
		facade.bind(pstmt, List.of(SqlParameter.create(num)));
		Mockito.verify(pstmt).setShort(Mockito.eq(ONE), Mockito.eq(num));

		Mockito.doReturn(num).when(rs).getShort(Mockito.eq(ONE));
		List<Short> list = facade.fetch(rs, Short.class);
		Assertions.assertEquals(ONE, list.size());
		Assertions.assertEquals(num, list.get(ZERO));
	}

}
