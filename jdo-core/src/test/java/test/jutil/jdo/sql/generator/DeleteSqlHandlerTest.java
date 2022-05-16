package test.jutil.jdo.sql.generator;

import io.jutil.jdo.internal.core.sql.SqlHandler;
import io.jutil.jdo.internal.core.sql.SqlRequest;
import io.jutil.jdo.internal.core.sql.SqlResponse;
import io.jutil.jdo.internal.core.sql.generator.DeleteSqlHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import test.jutil.jdo.model.GroupEntity;
import test.jutil.jdo.sql.SqlHandlerTest;

import java.util.List;

/**
 * @author Jin Zheng
 * @since 2022-02-21
 */
public class DeleteSqlHandlerTest extends SqlHandlerTest {
	private SqlResponse response = new SqlResponse(null);
	private SqlHandler handler = new DeleteSqlHandler();

	public DeleteSqlHandlerTest() {
	}

    @Test
    public void testSql1() {
		var config = metadataCache.loadEntityMetadata(GroupEntity.class);
	    var request = SqlRequest.create(GroupEntity.class, List.of(1), config);
	    handler.handle(request, response);
	    System.out.println(response.getSql());
	    Assertions.assertEquals("delete from `group` where `id`=?", response.getSql());
		Assertions.assertEquals(List.of(1), response.toParamList());
    }

	@Test
	public void testSql2() {
		var config = metadataCache.loadEntityMetadata(GroupEntity.class);
		var request = SqlRequest.create(GroupEntity.class, List.of(1, 2), config);
		handler.handle(request, response);
		System.out.println(response.getSql());
		Assertions.assertEquals("delete from `group` where `id` in (?,?)", response.getSql());
		Assertions.assertEquals(List.of(1, 2), response.toParamList());
	}

	@Test
	public void testSqlFailure1() {
		var config = metadataCache.loadEntityMetadata(GroupEntity.class);
		var request = SqlRequest.create(null, config);
		Assertions.assertThrows(NullPointerException.class, () -> handler.handle(request, response),
				"参数 不能为空");
	}

}
