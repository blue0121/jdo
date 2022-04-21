package test.jutil.jdo.sql.generator;

import io.jutil.jdo.core.exception.JdbcException;
import io.jutil.jdo.internal.core.sql.SqlHandler;
import io.jutil.jdo.internal.core.sql.SqlRequest;
import io.jutil.jdo.internal.core.sql.SqlResponse;
import io.jutil.jdo.internal.core.sql.generator.InsertSqlHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import test.jutil.jdo.model.GroupEntity;
import test.jutil.jdo.sql.SqlHandlerTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Jin Zheng
 * @since 2022-02-21
 */
public class InsertSqlHandlerTest extends SqlHandlerTest {
	private SqlResponse response = new SqlResponse(null);
	private SqlHandler handler = new InsertSqlHandler();

	public InsertSqlHandlerTest() {
	}

    @Test
    public void testSql1() {
		response.putParam("name", "blue");
		var config = configCache.loadEntityConfig(GroupEntity.class);
		var request = SqlRequest.create(null, config);
		handler.handle(request, response);
	    System.out.println(response.getSql());
	    Assertions.assertEquals("insert into `group` (`name`) values (?)", response.getSql());
		Assertions.assertEquals(List.of("name"), response.toNameList());
    }

	@Test
	public void testSql2() {
		response.putParam("name", "blue");
		response.putParam("count", 1);
		var config = configCache.loadEntityConfig(GroupEntity.class);
		var request = SqlRequest.create(null, config);
		handler.handle(request, response);
		System.out.println(response.getSql());
		Assertions.assertEquals("insert into `group` (`name`,`count`) values (?,?)", response.getSql());
		Assertions.assertEquals(List.of("name", "count"), response.toNameList());
	}

	@Test
	public void testSqlFailure1() {
		Map<String, Object> map = new HashMap<>();
		var config = configCache.loadEntityConfig(GroupEntity.class);
		var request = SqlRequest.create(null, config);
		Assertions.assertThrows(NullPointerException.class, () -> handler.handle(request, response),
				"参数 不能为空");
	}

	@Test
	public void testSqlFailure2() {
		response.putParam("aaa", "aaa");
		var config = configCache.loadEntityConfig(GroupEntity.class);
		var request = SqlRequest.create(null, config);
		Assertions.assertThrows(JdbcException.class, () -> handler.handle(request, response),
				"字段 [aaa] 不存在");
	}

}
