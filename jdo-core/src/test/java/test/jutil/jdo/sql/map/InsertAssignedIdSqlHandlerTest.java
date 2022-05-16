package test.jutil.jdo.sql.map;

import io.jutil.jdo.core.annotation.Entity;
import io.jutil.jdo.core.annotation.GeneratorType;
import io.jutil.jdo.core.annotation.Id;
import io.jutil.jdo.core.exception.EntityFieldException;
import io.jutil.jdo.internal.core.id.SnowflakeIdFactory;
import io.jutil.jdo.internal.core.sql.SqlHandler;
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
public class InsertAssignedIdSqlHandlerTest extends SqlHandlerTest {
	private SqlResponse response = new SqlResponse(null);
	private SqlHandler handler = new InsertIdSqlHandler(SnowflakeIdFactory.getSingleSnowflakeId());

	public InsertAssignedIdSqlHandlerTest() {
		parserFacade.parse(AssignedIntIdEntity.class);
		parserFacade.parse(AssignedLongIdEntity.class);
		parserFacade.parse(AssignedStringIdEntity.class);
	}

	@ParameterizedTest
	@CsvSource({"true,-1","true,10","false,-1","false,10"})
	public void testAssignedInt(boolean isEntity, int id) {
		var config = metadataCache.loadEntityMetadata(AssignedIntIdEntity.class);
		SqlRequest request = null;
		if (isEntity) {
			var entity = new AssignedIntIdEntity();
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
			Assertions.assertEquals(id, param.get("id"));
		} else {
			SqlRequest req = request;
			Assertions.assertThrows(EntityFieldException.class, () -> handler.handle(req, response));
		}
	}

	@ParameterizedTest
	@CsvSource({"true,-1","true,10","false,-1","false,10"})
	public void testAssignedLong(boolean isEntity, long id) {
		var config = metadataCache.loadEntityMetadata(AssignedLongIdEntity.class);
		SqlRequest request = null;
		if (isEntity) {
			var entity = new AssignedLongIdEntity();
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
			Assertions.assertEquals(id, param.get("id"));
		} else {
			SqlRequest req = request;
			Assertions.assertThrows(EntityFieldException.class, () -> handler.handle(req, response));
		}
	}

	@ParameterizedTest
	@CsvSource({"true,","true,abc","false,","false,abc"})
	public void testAssignedString(boolean isEntity, String id) {
		var config = metadataCache.loadEntityMetadata(AssignedStringIdEntity.class);
		SqlRequest request = null;
		if (isEntity) {
			var entity = new AssignedStringIdEntity();
			entity.id = id;
			request = SqlRequest.create(entity, config, true);
		} else {
			Map<String, Object> map = new HashMap<>();
			map.put("id", id);
			request = SqlRequest.create(map, config);
		}

		if (id != null && !id.isEmpty()) {
			handler.handle(request, response);
			var param = response.toParamMap();
			Assertions.assertEquals(1, param.size());
			Assertions.assertEquals(id, param.get("id"));
		} else {
			SqlRequest req = request;
			Assertions.assertThrows(EntityFieldException.class, () -> handler.handle(req, response));
		}
	}

	@Entity
	public class AssignedIntIdEntity {
		@Id(generator = GeneratorType.ASSIGNED)
		public Integer id;
		public String name;
	}

	@Entity
	public class AssignedLongIdEntity {
		@Id(generator = GeneratorType.ASSIGNED)
		public Long id;
		public String name;
	}

	@Entity
	public class AssignedStringIdEntity {
		@Id(generator = GeneratorType.ASSIGNED)
		public String id;
		public String name;
	}

}
