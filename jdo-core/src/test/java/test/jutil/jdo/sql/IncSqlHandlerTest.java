package test.jutil.jdo.sql;

import io.jutil.jdo.core.exception.JdbcException;
import io.jutil.jdo.internal.core.sql.SqlType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import test.jutil.jdo.model.GroupEntity;
import test.jutil.jdo.model.UserEntity;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Jin Zheng
 * @since 2022-02-21
 */
public class IncSqlHandlerTest extends SqlHandlerTest {

	public IncSqlHandlerTest() {
	}

    @Test
    public void testSql1() {
	    Map<String, Object> map = new HashMap<>();
		map.put("count", 1);
		var config = configCache.loadEntityConfig(GroupEntity.class);
		var sqlItem = factory.handle(SqlType.INC, config, map);
	    System.out.println(sqlItem.getSql());
	    Assertions.assertEquals("update `group` set `count`=`count`+? where `id`=?", sqlItem.getSql());
		Assertions.assertEquals(List.of("count", "id"), sqlItem.getParamNameList());
    }

	@Test
	public void testSql2() {
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("groupId", 1);
		var config = configCache.loadEntityConfig(UserEntity.class);
		var sqlItem = factory.handle(SqlType.INC, config, map);
		System.out.println(sqlItem.getSql());
		Assertions.assertEquals("update `usr_user` set `group_id`=`group_id`+? where `id`=?", sqlItem.getSql());
		Assertions.assertEquals(List.of("groupId", "id"), sqlItem.getParamNameList());
	}

	@Test
	public void testSqlFailure1() {
		Map<String, Object> map = new HashMap<>();
		var config = configCache.loadEntityConfig(GroupEntity.class);
		try {
			factory.handle(SqlType.INC, config, map);
			Assertions.fail();
		} catch (JdbcException e) {
			Assertions.assertEquals("参数不能为空", e.getMessage());
		}
	}

	@Test
	public void testSqlFailure2() {
		Map<String, Object> map = new HashMap<>();
		map.put("aaa", "aaa");
		var config = configCache.loadEntityConfig(GroupEntity.class);
		try {
			factory.handle(SqlType.INC, config, map);
			Assertions.fail();
		} catch (JdbcException e) {
			Assertions.assertEquals("字段 [aaa] 不存在", e.getMessage());
		}
	}

	@Test
	public void testSqlFailure3() {
		Map<String, Object> map = new HashMap<>();
		map.put("count", "aaa");
		var config = configCache.loadEntityConfig(GroupEntity.class);
		try {
			factory.handle(SqlType.INC, config, map);
			Assertions.fail();
		} catch (JdbcException e) {
			Assertions.assertEquals("参数 [count] 不是数字", e.getMessage());
		}
	}

	@Test
	public void testSqlFailure4() {
		Map<String, Object> map = new HashMap<>();
		map.put("name", 1);
		var config = configCache.loadEntityConfig(GroupEntity.class);
		try {
			factory.handle(SqlType.INC, config, map);
			Assertions.fail();
		} catch (JdbcException e) {
			Assertions.assertEquals("字段 [name] 不是数字", e.getMessage());
		}
	}

}
