package test.jutil.jdo.util;

import io.jutil.jdo.internal.core.util.StringUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Jin Zheng
 * @since 2022-02-18
 */
public class StringUtilTest {
	public StringUtilTest() {
	}

	@Test
	public void testGetJdbcType() {
		Assertions.assertEquals("mysql", StringUtil.getJdbcType("jdbc:mysql://localhost:3306/db"));
		Assertions.assertEquals("oracle", StringUtil.getJdbcType("jdbc:oracle:thin:@localhost:1521:db"));
		Assertions.assertEquals("sqlserver", StringUtil.getJdbcType("jdbc:sqlserver://localhost:1433;databasename=db"));
		Assertions.assertEquals("postgresql", StringUtil.getJdbcType("jdbc:postgresql://localhost:5432/db"));
	}

	@Test
	public void testJoin() {
		Assertions.assertEquals("a,b,c", StringUtil.join(Arrays.asList("a", "b", "c"), ","));
		Assertions.assertEquals("a", StringUtil.join(Arrays.asList("a"), ","));
		Assertions.assertEquals("abc", StringUtil.join(Arrays.asList("a", "b", "c"), ""));
		Assertions.assertEquals("abc", StringUtil.join(Arrays.asList("a", "b", "c"), null));
		Assertions.assertNull(StringUtil.join(null, null));
		Assertions.assertNull(StringUtil.join(new ArrayList<>(), null));
	}

	@Test
	public void testRepeat() {
		String repeat1 = StringUtil.repeat("?", 4, ",");
		Assertions.assertEquals("?,?,?,?", repeat1);

		String repeat2 = StringUtil.repeat("?", 4, null);
		Assertions.assertEquals("????", repeat2);

		String repeat3 = StringUtil.repeat("?", 1, null);
		Assertions.assertEquals("?", repeat3);
	}

}
