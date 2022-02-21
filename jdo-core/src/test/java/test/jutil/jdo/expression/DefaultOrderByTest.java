package test.jutil.jdo.expression;

import io.jutil.jdo.core.engine.OrderBy;
import io.jutil.jdo.internal.core.expression.DefaultOrderBy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Jin Zheng
 * @since 2022-02-21
 */
public class DefaultOrderByTest {
	public DefaultOrderByTest() {
	}

	@Test
	public void test1() {
		OrderBy order = new DefaultOrderBy();
		order.add("id desc");
		order.add("date asc");
		String str = order.toString();
		Assertions.assertEquals("id desc,date asc", str);
	}

	@Test
	public void test2() {
		OrderBy order = new DefaultOrderBy();
		order.add("id desc");
		String str = order.toString();
		Assertions.assertEquals("id desc", str);
	}

	@Test
	public void test3() {
		OrderBy order = new DefaultOrderBy();
		String str = order.toString();
		Assertions.assertTrue(str.isEmpty());
	}

}
