package test.jutil.jdo.sql.generator;

import io.jutil.jdo.core.exception.JdbcException;
import io.jutil.jdo.internal.core.sql.SqlHandler;
import io.jutil.jdo.internal.core.sql.SqlRequest;
import io.jutil.jdo.internal.core.sql.SqlResponse;
import io.jutil.jdo.internal.core.sql.generator.ExistSqlHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import test.jutil.jdo.model.GroupEntity;
import test.jutil.jdo.sql.SqlHandlerTest;

import java.util.List;

/**
 * @author Jin Zheng
 * @since 2022-02-21
 */
public class ExistSqlHandlerTest extends SqlHandlerTest {
	private SqlResponse response = new SqlResponse(null);
	private SqlHandler handler = new ExistSqlHandler();

	public ExistSqlHandlerTest() {
	}

	@Test
	public void testSql1() {
		response.putParam("name", "blue");
		response.putParam("id", 1);
		var config = configCache.loadEntityConfig(GroupEntity.class);
		var request = SqlRequest.create(null, List.of("name"), config);
		handler.handle(request, response);
		System.out.println(response.getSql());
		Assertions.assertEquals("select count(*) from `group` where `name`=? and `id`!=?", response.getSql());
		Assertions.assertEquals(List.of("name", "id"), response.toNameList());
	}

	@Test
	public void testSql2() {
		response.putParam("name", "blue");
		response.putParam("id", 1);
		var config = configCache.loadEntityConfig(GroupEntity.class);
		var request = SqlRequest.create(null, List.of(), config);
		handler.handle(request, response);
		System.out.println(response.getSql());
		Assertions.assertEquals("select count(*) from `group` where `id`=?", response.getSql());
		Assertions.assertEquals(List.of("id"), response.toNameList());
	}

	@Test
	public void testSql3() {
		response.putParam("name", "blue");
		var config = configCache.loadEntityConfig(GroupEntity.class);
		var request = SqlRequest.create(null, List.of("name"), config);
		handler.handle(request, response);
		System.out.println(response.getSql());
		Assertions.assertEquals("select count(*) from `group` where `name`=?", response.getSql());
		Assertions.assertEquals(List.of("name"), response.toNameList());
	}

	@Test
	public void testSqlFailure1() {
		var config = configCache.loadEntityConfig(GroupEntity.class);
		var request = SqlRequest.create(null, List.of(), config);
		Assertions.assertThrows(JdbcException.class, () -> handler.handle(request, response),
				"@Column 或 @Id 不能为空");
	}

	@Test
	public void testSqlFailure2() {
		response.putParam("aaa", "aaa");
		var config = configCache.loadEntityConfig(GroupEntity.class);
		var request = SqlRequest.create(null, List.of("aaa"), config);
		Assertions.assertThrows(JdbcException.class, () -> handler.handle(request, response),
				"字段 [aaa] 不存在");
	}

}
