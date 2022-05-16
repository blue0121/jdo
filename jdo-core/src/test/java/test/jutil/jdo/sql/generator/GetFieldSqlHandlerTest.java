package test.jutil.jdo.sql.generator;

import io.jutil.jdo.core.exception.JdbcException;
import io.jutil.jdo.internal.core.sql.SqlHandler;
import io.jutil.jdo.internal.core.sql.SqlRequest;
import io.jutil.jdo.internal.core.sql.SqlResponse;
import io.jutil.jdo.internal.core.sql.generator.GetFieldSqlHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import test.jutil.jdo.model.GroupEntity;
import test.jutil.jdo.sql.SqlHandlerTest;

import java.util.List;
import java.util.Map;

/**
 * @author Jin Zheng
 * @since 2022-02-21
 */
public class GetFieldSqlHandlerTest extends SqlHandlerTest {
	private SqlResponse response = new SqlResponse(null);
	private SqlHandler handler = new GetFieldSqlHandler();

	public GetFieldSqlHandlerTest() {
	}

    @Test
    public void testSql1() {
		response.putParam("id", 1);
		var config = metadataCache.loadEntityMetadata(GroupEntity.class);
		var request = SqlRequest.create("name", Map.of(), config);
		handler.handle(request, response);
	    System.out.println(response.getSql());
	    Assertions.assertEquals("select `name` from `group` where `id`=?", response.getSql());
		Assertions.assertEquals(List.of("id"), response.toNameList());
    }

	@Test
	public void testSql2() {
		response.putParam("id", 1);
		response.putParam("count", 1);
		var config = metadataCache.loadEntityMetadata(GroupEntity.class);
		var request = SqlRequest.create("name", Map.of(), config);
		handler.handle(request, response);
		System.out.println(response.getSql());
		Assertions.assertEquals("select `name` from `group` where `count`=? and `id`=?", response.getSql());
		Assertions.assertEquals(List.of("count", "id"), response.toNameList());
	}

	@Test
	public void testSqlFailure1() {
		var config = metadataCache.loadEntityMetadata(GroupEntity.class);
		var request = SqlRequest.create("name", Map.of(), config);
		Assertions.assertThrows(NullPointerException.class, () -> handler.handle(request, response),
				"参数 不能为空");
	}

	@Test
	public void testSqlFailure2() {
		response.putParam("aaa", "aaa");
		var config = metadataCache.loadEntityMetadata(GroupEntity.class);
		var request = SqlRequest.create("name", Map.of(), config);
		Assertions.assertThrows(JdbcException.class, () -> handler.handle(request, response),
				"字段 [aaa] 不存在");
	}

}
