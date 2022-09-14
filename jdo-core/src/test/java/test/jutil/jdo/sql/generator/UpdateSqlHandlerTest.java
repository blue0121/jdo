package test.jutil.jdo.sql.generator;

import io.jutil.jdo.core.exception.JdbcException;
import io.jutil.jdo.internal.core.sql.SqlHandler;
import io.jutil.jdo.internal.core.sql.SqlRequest;
import io.jutil.jdo.internal.core.sql.SqlResponse;
import io.jutil.jdo.internal.core.sql.generator.UpdateSqlHandler;
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
public class UpdateSqlHandlerTest extends SqlHandlerTest {
	private SqlResponse response = new SqlResponse(null);
	private SqlHandler handler = new UpdateSqlHandler();

	public UpdateSqlHandlerTest() {
	}

    @Test
    public void testSql1() {
		response.putParam("name", "blue");
		response.putParam("id", 1);
		var config = metadataCache.loadEntityMetadata(GroupEntity.class);
		var request = SqlRequest.create(null, config);
		handler.handle(request, response);
	    System.out.println(response.getSql());
	    Assertions.assertEquals("update usr_group set name=? where id=?", response.getSql());
		Assertions.assertEquals(List.of("name", "id"), response.toNameList());
    }

	@Test
	public void testSql2() {
		response.putParam("name", "blue");
		response.putParam("count", 1);
		response.putParam("id", 1);
		var config = metadataCache.loadEntityMetadata(GroupEntity.class);
		var request = SqlRequest.create(null, config);
		handler.handle(request, response);
		System.out.println(response.getSql());
		Assertions.assertEquals("update usr_group set name=?,count=? where id=?", response.getSql());
		Assertions.assertEquals(List.of("name", "count", "id"), response.toNameList());
	}

	@Test
	public void testSql3() {
		response.putParam("name", "blue");
		response.putParam("id", 1);
		response.putParam("version", 1);
		var config = metadataCache.loadEntityMetadata(UserEntity.class);
		var request = SqlRequest.create(null, config);
		handler.handle(request, response);
		System.out.println(response.getSql());
		Assertions.assertEquals("update usr_user set name=?,version=version+1 where id=? and version=?",
				response.getSql());
		Assertions.assertEquals(List.of("name", "id", "version"), response.toNameList());
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

	@Test
	public void testSqlFailure3() {
		response.putParam("name", "blue");
		var config = metadataCache.loadEntityMetadata(GroupEntity.class);
		var request = SqlRequest.create(null, config);
		Assertions.assertThrows(JdbcException.class, () -> handler.handle(request, response),
				"@Id 不能为空");
	}

	@Test
	public void testSqlFailure4() {
		response.putParam("id", 1);
		var config = metadataCache.loadEntityMetadata(GroupEntity.class);
		var request = SqlRequest.create(null, config);
		Assertions.assertThrows(JdbcException.class, () -> handler.handle(request, response),
				"@Column 不能为空");
	}

}
