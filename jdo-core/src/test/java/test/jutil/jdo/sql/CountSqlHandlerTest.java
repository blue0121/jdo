package test.jutil.jdo.sql;

import io.jutil.jdo.core.exception.JdbcException;
import io.jutil.jdo.internal.core.sql.SqlType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import test.jutil.jdo.model.GroupEntity;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Jin Zheng
 * @since 2022-02-21
 */
public class CountSqlHandlerTest extends SqlHandlerTest {

	public CountSqlHandlerTest() {
	}

    @Test
    public void testSql1() {
	    Map<String, Object> map = new HashMap<>();
		map.put("name", "blue");
		var config = entityConfigCache.get(GroupEntity.class);
		var sqlItem = factory.handle(SqlType.COUNT, config, map);
	    System.out.println(sqlItem.getSql());
	    Assertions.assertEquals("select count(*) from `group` where `name`=?", sqlItem.getSql());
		Assertions.assertEquals(List.of("name"), sqlItem.getParamNameList());
    }

	@Test
	public void testSql2() {
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("name", "blue");
		map.put("count", 1);
		var config = entityConfigCache.get(GroupEntity.class);
		var sqlItem = factory.handle(SqlType.COUNT, config, map);
		System.out.println(sqlItem.getSql());
		Assertions.assertEquals("select count(*) from `group` where `name`=? and `count`=?", sqlItem.getSql());
		Assertions.assertEquals(List.of("name", "count"), sqlItem.getParamNameList());
	}

	@Test
	public void testSqlFailure1() {
		Map<String, Object> map = new HashMap<>();
		var config = entityConfigCache.get(GroupEntity.class);
		try {
			factory.handle(SqlType.COUNT, config, map);
			Assertions.fail();
		} catch (JdbcException e) {
			Assertions.assertEquals("参数不能为空", e.getMessage());
		}
	}

	@Test
	public void testSqlFailure2() {
		Map<String, Object> map = new HashMap<>();
		map.put("aaa", "aaa");
		var config = entityConfigCache.get(GroupEntity.class);
		try {
			factory.handle(SqlType.COUNT, config, map);
			Assertions.fail();
		} catch (JdbcException e) {
			Assertions.assertEquals("字段 [aaa] 不存在", e.getMessage());
		}
	}

}
