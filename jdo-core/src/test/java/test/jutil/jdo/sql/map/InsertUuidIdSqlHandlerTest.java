package test.jutil.jdo.sql.map;

import io.jutil.jdo.core.annotation.Entity;
import io.jutil.jdo.core.annotation.GeneratorType;
import io.jutil.jdo.core.annotation.Id;
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
public class InsertUuidIdSqlHandlerTest extends SqlHandlerTest {
	private SqlResponse response = new SqlResponse(null);
	private SqlHandler handler = new InsertIdSqlHandler(SnowflakeIdFactory.getSingleSnowflakeId());

	public InsertUuidIdSqlHandlerTest() {
		parserFacade.parse(UuidStringIdEntity.class);
	}

	@ParameterizedTest
	@CsvSource({"true,","true,abc","false,","false,abc"})
	public void testUuidString(boolean isEntity, String id) {
		var config = metadataCache.loadEntityMetadata(UuidStringIdEntity.class);
		SqlRequest request = null;
		if (isEntity) {
			var entity = new UuidStringIdEntity();
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
	public class UuidStringIdEntity {
		@Id(generator = GeneratorType.UUID)
		public String id;
		public String name;
	}
}
