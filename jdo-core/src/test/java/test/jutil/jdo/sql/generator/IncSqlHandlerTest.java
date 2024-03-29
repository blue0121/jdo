package test.jutil.jdo.sql.generator;

import io.jutil.jdo.core.exception.JdbcException;
import io.jutil.jdo.internal.core.sql.SqlHandler;
import io.jutil.jdo.internal.core.sql.SqlRequest;
import io.jutil.jdo.internal.core.sql.SqlResponse;
import io.jutil.jdo.internal.core.sql.generator.IncSqlHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import test.jutil.jdo.model.GroupEntity;
import test.jutil.jdo.model.UserEntity;
import test.jutil.jdo.sql.SqlHandlerTest;

import java.util.List;

/**
 * @author Jin Zheng
 * @since 2022-02-21
 */
public class IncSqlHandlerTest extends SqlHandlerTest {
	private SqlResponse response = new SqlResponse(null);
	private SqlHandler handler = new IncSqlHandler();

	public IncSqlHandlerTest() {
	}

    @Test
    public void testSql1() {
		response.putParam("count", 1);
		var config = metadataCache.loadEntityMetadata(GroupEntity.class);
	    var request = SqlRequest.create(null, config);
	    handler.handle(request, response);
	    System.out.println(response.getSql());
	    Assertions.assertEquals("update usr_group set count=count+? where id=?", response.getSql());
		Assertions.assertEquals(List.of("count", "id"), response.toNameList());
    }

	@Test
	public void testSql2() {
		response.putParam("groupId", 1);
		var config = metadataCache.loadEntityMetadata(UserEntity.class);
		var request = SqlRequest.create(null, config);
		handler.handle(request, response);
		System.out.println(response.getSql());
		Assertions.assertEquals("update usr_user set group_id=group_id+? where id=?", response.getSql());
		Assertions.assertEquals(List.of("groupId", "id"), response.toNameList());
	}

	@Test
	public void testSqlFailure1() {
		var config = metadataCache.loadEntityMetadata(GroupEntity.class);
		var request = SqlRequest.create(null, config);
		Assertions.assertThrows(NullPointerException.class, () -> handler.handle(request, response),
				"参数 不能为空");
	}

	@Test
	public void testSqlFailure3() {
		response.putParam("count", "aaa");
		var config = metadataCache.loadEntityMetadata(GroupEntity.class);
		var request = SqlRequest.create(null, config);
		Assertions.assertThrows(JdbcException.class, () -> handler.handle(request, response),
				"字段 [aaa] 不是数字");
	}

	@Test
	public void testSqlFailure4() {
		response.putParam("name", 1);
		var config = metadataCache.loadEntityMetadata(GroupEntity.class);
		var request = SqlRequest.create(null, config);
		Assertions.assertThrows(JdbcException.class, () -> handler.handle(request, response),
				"字段 [name] 不是数字");
	}

}
