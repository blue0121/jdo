package test.jutil.jdo.sql.map;

import io.jutil.jdo.core.annotation.Entity;
import io.jutil.jdo.core.annotation.Id;
import io.jutil.jdo.core.exception.EntityFieldException;
import io.jutil.jdo.internal.core.sql.SqlHandler;
import io.jutil.jdo.internal.core.sql.SqlRequest;
import io.jutil.jdo.internal.core.sql.SqlResponse;
import io.jutil.jdo.internal.core.sql.map.UpdateIdSqlHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import test.jutil.jdo.sql.SqlHandlerTest;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jin Zheng
 * @since 2022-04-20
 */
public class UpdateIdSqlHandlerTest extends SqlHandlerTest {
	private SqlResponse response = new SqlResponse(null);
	private SqlHandler handler = new UpdateIdSqlHandler();

	public UpdateIdSqlHandlerTest() {
		parseFactory.parse(IdEntity.class);
	}

	@ParameterizedTest
	@CsvSource({"true,-1","true,10","false,-1","false,10"})
	public void testHandle(boolean isEntity, int id) {
		var config = configCache.loadEntityConfig(IdEntity.class);
		SqlRequest request = null;
		if (isEntity) {
			var entity = new IdEntity();
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

		if (id > 0) {
			handler.handle(request, response);
			var param = response.toParamMap();
			Assertions.assertEquals(1, param.size());
			Assertions.assertEquals(10, param.get("id"));
		} else {
			SqlRequest req = request;
			Assertions.assertThrows(EntityFieldException.class, () -> handler.handle(req, response));
		}
	}

	@Test
	public void testHandleMap() {
		Map<String, Object> map = new HashMap<>();
		map.put("test", "test");
		var config = configCache.loadEntityConfig(IdEntity.class);
		var request = SqlRequest.create(map, config);
		Assertions.assertThrows(EntityFieldException.class, () -> handler.handle(request, response));
	}

	@Entity
	public class IdEntity {
		@Id
		public Integer id;
		public String name;
	}

}
