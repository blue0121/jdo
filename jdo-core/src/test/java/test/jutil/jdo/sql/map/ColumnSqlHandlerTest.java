package test.jutil.jdo.sql.map;

import io.jutil.jdo.core.annotation.Entity;
import io.jutil.jdo.core.annotation.Id;
import io.jutil.jdo.internal.core.sql.SqlHandle;
import io.jutil.jdo.internal.core.sql.SqlRequest;
import io.jutil.jdo.internal.core.sql.SqlResponse;
import io.jutil.jdo.internal.core.sql.map.ColumnSqlHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import test.jutil.jdo.sql.SqlHandlerTest;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jin Zheng
 * @since 2022-04-20
 */
public class ColumnSqlHandlerTest extends SqlHandlerTest {
	private SqlResponse response = new SqlResponse(null);
	private SqlHandle handler = new ColumnSqlHandler();

	public ColumnSqlHandlerTest() {
		parseFactory.parse(ColumnEntity.class);
	}

	@Test
	public void testHandleObject1() {
		var column = new ColumnEntity();
		var config = configCache.loadEntityConfig(ColumnEntity.class);
		var request = SqlRequest.create(column, config, false);
		handler.handle(request, response);
		var param = response.toParamMap();
		Assertions.assertEquals(1, param.size());
		Assertions.assertTrue(param.containsKey("name"));
		Assertions.assertNull(param.get("name"));
	}

	@Test
	public void testHandleObject2() {
		var column = new ColumnEntity();
		var config = configCache.loadEntityConfig(ColumnEntity.class);
		var request = SqlRequest.create(column, config, true);
		handler.handle(request, response);
		var param = response.toParamMap();
		Assertions.assertTrue(param.isEmpty());
	}

	@Test
	public void testHandleMap1() {
		Map<String, Object> map = new HashMap<>();
		var config = configCache.loadEntityConfig(ColumnEntity.class);
		var request = SqlRequest.create(map, config);
		handler.handle(request, response);
		var param = response.toParamMap();
		Assertions.assertTrue(param.isEmpty());
	}

	@Test
	public void testHandleMap2() {
		Map<String, Object> map = new HashMap<>();
		map.put("test", "test");
		var config = configCache.loadEntityConfig(ColumnEntity.class);
		var request = SqlRequest.create(map, config);
		handler.handle(request, response);
		var param = response.toParamMap();
		Assertions.assertTrue(param.isEmpty());
	}

	@Entity
	public class ColumnEntity {
		@Id
		public Integer id;
		public String name;
	}

}
