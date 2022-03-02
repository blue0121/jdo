package test.jutil.jdo.collection;

import io.jutil.jdo.core.collection.Page;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Jin Zheng
 * @since 2022-02-28
 */
public class PageTest
{
	public PageTest()
	{
	}
	
	@Test
	public void test1()
	{
		Page page = new Page(2, 1);
		page.setTotalResult(10);
		Assertions.assertEquals(5, page.getTotalPage());
		Assertions.assertEquals(10, page.getTotalResult());
		Assertions.assertEquals(0, page.getRowIndex());
		page.setPageIndex(2);
		Assertions.assertEquals(5, page.getTotalPage());
		Assertions.assertEquals(10, page.getTotalResult());
		Assertions.assertEquals(2, page.getRowIndex());
		page.setPageIndex(3);
		Assertions.assertEquals(5, page.getTotalPage());
		Assertions.assertEquals(10, page.getTotalResult());
		Assertions.assertEquals(4, page.getRowIndex());
	}

	@Test
	public void test2()
	{
		Page page = new Page(3, 2);
		page.setTotalResult(10);
		Assertions.assertEquals(4, page.getTotalPage());
		Assertions.assertEquals(10, page.getTotalResult());
		Assertions.assertEquals(3, page.getRowIndex());
	}
	
}
