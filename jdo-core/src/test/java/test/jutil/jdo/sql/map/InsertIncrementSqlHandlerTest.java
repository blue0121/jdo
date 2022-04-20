package test.jutil.jdo.sql.map;

import io.jutil.jdo.core.annotation.Entity;
import io.jutil.jdo.core.annotation.GeneratorType;
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
public class InsertIncrementSqlHandlerTest extends SqlHandlerTest {
    private SqlResponse response = new SqlResponse(null);
    private SqlHandle handler = new InsertIdSqlHandler(SnowflakeIdFactory.getSingleSnowflakeId());

	public InsertIncrementSqlHandlerTest() {
        parseFactory.parse(IncrementIntIdEntity.class);
        parseFactory.parse(IncrementLongIdEntity.class);
        parseFactory.parse(IncrementStringIdEntity.class);
	}

    @ParameterizedTest
    @CsvSource({"true,-1","true,10","false,-1","false,10"})
    public void testIncrementInt(boolean isEntity, int id) {
        var config = configCache.loadEntityConfig(IncrementIntIdEntity.class);
        SqlRequest request = null;
        if (isEntity) {
            var entity = new IncrementIntIdEntity();
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
    public void testIncrementLong(boolean isEntity, long id) {
        var config = configCache.loadEntityConfig(IncrementLongIdEntity.class);
        SqlRequest request = null;
        if (isEntity) {
            var entity = new IncrementLongIdEntity();
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
    public void testIncrementString(boolean isEntity, String id) {
        var config = configCache.loadEntityConfig(IncrementStringIdEntity.class);
        SqlRequest request = null;
        if (isEntity) {
            var entity = new IncrementStringIdEntity();
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
        SqlRequest req = request;
        Assertions.assertThrows(UnsupportedOperationException.class, () -> handler.handle(req, response));
    }

    @Entity
    public class IncrementIntIdEntity {
        @Id(generator = GeneratorType.INCREMENT)
        public Integer id;
        public String name;
    }

    @Entity
    public class IncrementLongIdEntity {
        @Id(generator = GeneratorType.INCREMENT)
        public Long id;
        public String name;
    }

    @Entity
    public class IncrementStringIdEntity {
        @Id(generator = GeneratorType.INCREMENT)
        public String id;
        public String name;
    }
}
