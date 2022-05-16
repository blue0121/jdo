package test.jutil.jdo.sql.map;

import io.jutil.jdo.core.annotation.Entity;
import io.jutil.jdo.core.annotation.Id;
import io.jutil.jdo.internal.core.sql.SqlHandler;
import io.jutil.jdo.internal.core.sql.SqlRequest;
import io.jutil.jdo.internal.core.sql.SqlResponse;
import io.jutil.jdo.internal.core.sql.map.IdSqlHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import test.jutil.jdo.sql.SqlHandlerTest;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jin Zheng
 * @since 2022-04-20
 */
public class IdSqlHandlerTest extends SqlHandlerTest {
	private SqlResponse response = new SqlResponse(null);
	private SqlHandler handler = new IdSqlHandler();

	public IdSqlHandlerTest() {
		parserFacade.parse(IdEntity.class);
	}

	@Test
	public void testHandleObject1() {
		var column = new IdEntity();
		var config = metadataCache.loadEntityMetadata(IdEntity.class);
		var request = SqlRequest.create(column, config, false);
		handler.handle(request, response);
		var param = response.toParamMap();
		Assertions.assertEquals(1, param.size());
		Assertions.assertTrue(param.containsKey("id"));
		Assertions.assertNull(param.get("id"));
	}

	@Test
	public void testHandleObject2() {
		var column = new IdEntity();
		var config = metadataCache.loadEntityMetadata(IdEntity.class);
		var request = SqlRequest.create(column, config, true);
		handler.handle(request, response);
		var param = response.toParamMap();
		Assertions.assertTrue(param.isEmpty());
	}

	@Test
	public void testHandleMap1() {
		Map<String, Object> map = new HashMap<>();
		var config = metadataCache.loadEntityMetadata(IdEntity.class);
		var request = SqlRequest.create(map, config);
		handler.handle(request, response);
		var param = response.toParamMap();
		Assertions.assertTrue(param.isEmpty());
	}

	@Test
	public void testHandleMap2() {
		Map<String, Object> map = new HashMap<>();
		map.put("test", "test");
		var config = metadataCache.loadEntityMetadata(IdEntity.class);
		var request = SqlRequest.create(map, config);
		handler.handle(request, response);
		var param = response.toParamMap();
		Assertions.assertTrue(param.isEmpty());
	}

	@Entity
	public class IdEntity {
		@Id
		public Integer id;
		public String name;
	}

}
