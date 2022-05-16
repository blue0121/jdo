package test.jutil.jdo.sql.generator;

import io.jutil.jdo.core.exception.JdbcException;
import io.jutil.jdo.internal.core.sql.SqlHandler;
import io.jutil.jdo.internal.core.sql.SqlRequest;
import io.jutil.jdo.internal.core.sql.SqlResponse;
import io.jutil.jdo.internal.core.sql.generator.DeleteBySqlHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import test.jutil.jdo.model.GroupEntity;
import test.jutil.jdo.sql.SqlHandlerTest;

import java.util.List;

/**
 * @author Jin Zheng
 * @since 2022-02-21
 */
public class DeleteBySqlHandlerTest extends SqlHandlerTest {
	private SqlResponse response = new SqlResponse(null);
	private SqlHandler handler = new DeleteBySqlHandler();

	public DeleteBySqlHandlerTest() {
	}

    @Test
    public void testSql1() {
	    response.putParam("name", "blue");
		var config = metadataCache.loadEntityMetadata(GroupEntity.class);
	    var request = SqlRequest.create(null, config);
	    handler.handle(request, response);
	    System.out.println(response.getSql());
	    Assertions.assertEquals("delete from `group` where `name`=?", response.getSql());
		Assertions.assertEquals(List.of("name"), response.toNameList());
    }

	@Test
	public void testSql2() {
		response.putParam("name", "blue");
		response.putParam("count", 1);
		var config = metadataCache.loadEntityMetadata(GroupEntity.class);
		var request = SqlRequest.create(null, config);
		handler.handle(request, response);
		System.out.println(response.getSql());
		Assertions.assertEquals("delete from `group` where `name`=? and `count`=?", response.getSql());
		Assertions.assertEquals(List.of("name", "count"), response.toNameList());
	}

	@Test
	public void testSqlFailure1() {
		var config = metadataCache.loadEntityMetadata(GroupEntity.class);
		var request = SqlRequest.create(null, config);
		Assertions.assertThrows(NullPointerException.class, () -> handler.handle(request, response),
				"参数 不能为空");
	}

	@Test
	public void testSqlFailure2() {
		response.putParam("aaa", "aaa");
		var config = metadataCache.loadEntityMetadata(GroupEntity.class);
		var request = SqlRequest.create(null, config);
		Assertions.assertThrows(JdbcException.class, () -> handler.handle(request, response),
				"字段 [aaa] 不存在");
	}

}
