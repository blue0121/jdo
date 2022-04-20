package test.jutil.jdo.sql.map;

import io.jutil.jdo.core.annotation.Entity;
import io.jutil.jdo.core.annotation.Id;
import io.jutil.jdo.internal.core.id.SnowflakeIdFactory;
import io.jutil.jdo.internal.core.sql.SqlHandle;
import io.jutil.jdo.internal.core.sql.SqlRequest;
import io.jutil.jdo.internal.core.sql.SqlResponse;
import io.jutil.jdo.internal.core.sql.map.InsertIdSqlHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import test.jutil.jdo.sql.SqlHandlerTest;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jin Zheng
 * @since 2022-04-20
 */
public class InsertAutoIdSqlHandlerTest extends SqlHandlerTest {
	private SqlResponse response = new SqlResponse(null);
	private SqlHandle handler = new InsertIdSqlHandler(SnowflakeIdFactory.getSingleSnowflakeId());

	public InsertAutoIdSqlHandlerTest() {
		parseFactory.parse(AutoIntIdEntity.class);
		parseFactory.parse(AutoLongIdEntity.class);
		parseFactory.parse(AutoStringIdEntity.class);
	}

	@ParameterizedTest
	@CsvSource({"true,-1","true,10","false,-1","false,10"})
	public void testAutoInt(boolean isEntity, int id) {
		var config = configCache.loadEntityConfig(AutoIntIdEntity.class);
		SqlRequest request = null;
		if (isEntity) {
			var entity = new AutoIntIdEntity();
			if (id > 0) {
				entity.id = id;
			}
			request = SqlRequest.create(entity, config, true);
		} else {
			Map<String, Object> map = new HashMap<>();
			if (id > 0) {
				map.put("id", id);
			}
			request = SqlRequest.create(map, config);
		}
		handler.handle(request, response);
		var param = response.toParamMap();
		Assertions.assertTrue(param.isEmpty());
	}

	@ParameterizedTest
	@CsvSource({"true,-1","true,10","false,-1","false,10"})
	public void testAutoLong(boolean isEntity, long id) {
		var config = configCache.loadEntityConfig(AutoLongIdEntity.class);
		SqlRequest request = null;
		if (isEntity) {
			var entity = new AutoLongIdEntity();
			if (id > 0) {
				entity.id = id;
			}
			request = SqlRequest.create(entity, config, true);
		} else {
			Map<String, Object> map = new HashMap<>();
			if (id > 0) {
				map.put("id", id);
			}
			request = SqlRequest.create(map, config);
		}
		handler.handle(request, response);
		var param = response.toParamMap();
		Assertions.assertTrue(param.isEmpty());
	}

	@ParameterizedTest
	@CsvSource({"true,","true,abc","false,","false,abc"})
	public void testAutoString(boolean isEntity, String id) {
		var config = configCache.loadEntityConfig(AutoStringIdEntity.class);
		SqlRequest request = null;
		if (isEntity) {
			var entity = new AutoStringIdEntity();
			if (id != null && !id.isEmpty()) {
				entity.id = id;
			}
			request = SqlRequest.create(entity, config, true);
		} else {
			Map<String, Object> map = new HashMap<>();
			if (id != null && !id.isEmpty()) {
				map.put("id", id);
			}
			request = SqlRequest.create(map, config);
		}
		handler.handle(request, response);
		var param = response.toParamMap();
		Assertions.assertEquals(1, param.size());
		Assertions.assertEquals(32, param.get("id").toString().length());
	}

	@Entity
	public class AutoIntIdEntity {
		@Id
		public Integer id;
		public String name;
	}

	@Entity
	public class AutoLongIdEntity {
		@Id
		public Long id;
		public String name;
	}

	@Entity
	public class AutoStringIdEntity {
		@Id
		public String id;
		public String name;
	}

}
