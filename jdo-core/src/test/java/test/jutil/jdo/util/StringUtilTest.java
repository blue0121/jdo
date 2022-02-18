package test.jutil.jdo.util;

import io.jutil.jdo.internal.core.util.StringUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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

}
