package test.jutil.jdo.executor.parameter;

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
		var num = new BigDecimal(1);
		factory.bind(pstmt, List.of(num));
		Mockito.verify(pstmt).setBigDecimal(Mockito.eq(1), Mockito.eq(num));
	}

	@Test
	public void testBigInteger() throws SQLException {
		var num = new BigInteger("1");
		factory.bind(pstmt, List.of(num));
		Mockito.verify(pstmt).setBigDecimal(Mockito.eq(1), Mockito.eq(new BigDecimal(1)));
	}

	@Test
	public void testByteArray() throws SQLException {
		var num = new byte[] {0x1, 0x2};
		factory.bind(pstmt, List.of(num));
		Mockito.verify(pstmt).setBytes(Mockito.eq(1), Mockito.eq(num));
	}

	@Test
	public void testByte() throws SQLException {
		var num = (byte)1;
		factory.bind(pstmt, List.of(num));
		Mockito.verify(pstmt).setByte(Mockito.eq(1), Mockito.eq(num));
	}

	@Test
	public void testDouble() throws SQLException {
		var num = 1.0d;
		factory.bind(pstmt, List.of(num));
		Mockito.verify(pstmt).setDouble(Mockito.eq(1), Mockito.eq(num));
	}

	@Test
	public void testFloat() throws SQLException {
		var num = 1.0f;
		factory.bind(pstmt, List.of(num));
		Mockito.verify(pstmt).setFloat(Mockito.eq(1), Mockito.eq(num));
	}

	@Test
	public void testInteger() throws SQLException {
		var num = 1;
		factory.bind(pstmt, List.of(num));
		Mockito.verify(pstmt).setInt(Mockito.eq(1), Mockito.eq(num));
	}

	@Test
	public void testLong() throws SQLException {
		var num = 1L;
		factory.bind(pstmt, List.of(num));
		Mockito.verify(pstmt).setLong(Mockito.eq(1), Mockito.eq(num));
	}

	@Test
	public void testShort() throws SQLException {
		var num = (short)1;
		factory.bind(pstmt, List.of(num));
		Mockito.verify(pstmt).setShort(Mockito.eq(1), Mockito.eq(num));
	}

}
