package test.jutil.jdo.sql.map;

import io.jutil.jdo.core.annotation.Entity;
import io.jutil.jdo.core.annotation.Id;
import io.jutil.jdo.core.annotation.Version;
import io.jutil.jdo.internal.core.sql.SqlHandler;
import io.jutil.jdo.internal.core.sql.SqlRequest;
import io.jutil.jdo.internal.core.sql.SqlResponse;
import io.jutil.jdo.internal.core.sql.map.InsertVersionSqlHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import test.jutil.jdo.sql.SqlHandlerTest;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jin Zheng
 * @since 2022-04-20
 */
public class InsertVersionSqlHandlerTest extends SqlHandlerTest {
	private SqlResponse response = new SqlResponse(null);
	private SqlHandler handler = new InsertVersionSqlHandler();

	public InsertVersionSqlHandlerTest() {
		parserFacade.parse(ForceVersionEntity.class);
	}

	@Test
	public void testHandleForceVersionEntity1() {
		var version = new ForceVersionEntity();
		var config = metadataCache.loadEntityMetadata(ForceVersionEntity.class);
		var request = SqlRequest.create(version, config, false);
		handler.handle(request, response);
		var param = response.toParamMap();
		Assertions.assertEquals(1, param.size());
		Assertions.assertEquals(1, param.get("version"));
	}

	@Test
	public void testHandleForceVersionEntity2() {
		var version = new ForceVersionEntity();
		version.version = 10;
		var config = metadataCache.loadEntityMetadata(ForceVersionEntity.class);
		var request = SqlRequest.create(version, config, false);
		handler.handle(request, response);
		var param = response.toParamMap();
		Assertions.assertEquals(1, param.size());
		Assertions.assertEquals(10, param.get("version"));
	}

	@Test
	public void testHandleMap1() {
		Map<String, Object> map = new HashMap<>();
		var config = metadataCache.loadEntityMetadata(ForceVersionEntity.class);
		var request = SqlRequest.create(map, config);
		handler.handle(request, response);
		var param = response.toParamMap();
		Assertions.assertEquals(1, param.size());
		Assertions.assertEquals(1, param.get("version"));
	}

	@Test
	public void testHandleMap2() {
		Map<String, Object> map = new HashMap<>();
		map.put("version", 10);
		var config = metadataCache.loadEntityMetadata(ForceVersionEntity.class);
		var request = SqlRequest.create(map, config);
		handler.handle(request, response);
		var param = response.toParamMap();
		Assertions.assertEquals(1, param.size());
		Assertions.assertEquals(10, param.get("version"));
	}

	@Entity
	public class ForceVersionEntity {
		@Id
		public Integer id;
		@Version
		public Integer version;
		public String name;
	}

}
