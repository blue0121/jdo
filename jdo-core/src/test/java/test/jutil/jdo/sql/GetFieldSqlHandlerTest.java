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
public class GetFieldSqlHandlerTest extends SqlHandlerTest {

	public GetFieldSqlHandlerTest() {
	}

    @Test
    public void testSql1() {
	    Map<String, Object> map = new HashMap<>();
		map.put("id", 1);
		var config = entityConfigCache.get(GroupEntity.class);
		var sqlItem = factory.handle(SqlType.GET_FIELD, config, "name", map);
	    System.out.println(sqlItem.getSql());
	    Assertions.assertEquals("select `name` from `group` where `id`=?",
			    sqlItem.getSql());
		Assertions.assertEquals(List.of("id"), sqlItem.getParamNameList());
    }

	@Test
	public void testSql2() {
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("id", 1);
		map.put("count", 1);
		var config = entityConfigCache.get(GroupEntity.class);
		var sqlItem = factory.handle(SqlType.GET_FIELD, config, "name", map);
		System.out.println(sqlItem.getSql());
		Assertions.assertEquals("select `name` from `group` where `id`=? and `count`=?",
				sqlItem.getSql());
		Assertions.assertEquals(List.of("id", "count"), sqlItem.getParamNameList());
	}

	@Test
	public void testSqlFailure1() {
		Map<String, Object> map = new HashMap<>();
		var config = entityConfigCache.get(GroupEntity.class);
		try {
			factory.handle(SqlType.GET_FIELD, config, "name", map);
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
			factory.handle(SqlType.GET_FIELD, config, "name", map);
			Assertions.fail();
		} catch (JdbcException e) {
			Assertions.assertEquals("字段 [aaa] 不存在", e.getMessage());
		}
	}

}
