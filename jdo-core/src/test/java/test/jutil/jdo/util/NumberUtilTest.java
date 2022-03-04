package test.jutil.jdo.util;

import io.jutil.jdo.internal.core.util.NumberUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * @author Jin Zheng
 * @since 2022-03-04
 */
public class NumberUtilTest {
	public NumberUtilTest() {
	}

	@Test
	public void testToHexString() {
		byte[] bytes = new byte[]{-128, 0, 127};
		String hex = NumberUtil.toHexString(bytes);
		System.out.println(hex);
		Assertions.assertEquals("80007f", hex);
	}

	@ParameterizedTest
	@CsvSource({"java.lang.Long,true",
			"java.lang.Integer,true",
			"java.lang.Short,true",
			"java.lang.Byte,true",
			"java.lang.Float,true",
			"java.lang.Double,true",
			"java.lang.Boolean,false"})
	public void testIsNumber(String str, boolean isTrue) throws ClassNotFoundException {
		var clazz = Class.forName(str);
		Assertions.assertEquals(isTrue, NumberUtil.isNumber(clazz));
	}

	@ParameterizedTest
	@CsvSource({"1,0001","127,007f","128,0080"})
	public void testShortToHexString(short val, String hex) {
		Assertions.assertEquals(hex, NumberUtil.toHexString(val));
	}

	@ParameterizedTest
	@CsvSource({"1,00000001","127,0000007f","128,00000080"})
	public void testIntToHexString(int val, String hex) {
		Assertions.assertEquals(hex, NumberUtil.toHexString(val));
	}

	@ParameterizedTest
	@CsvSource({"1,0000000000000001","127,000000000000007f","128,0000000000000080"})
	public void testLongToHexString(long val, String hex) {
		Assertions.assertEquals(hex, NumberUtil.toHexString(val));
	}

}
