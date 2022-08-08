package test.jutil.jdo.sql.parameter;

import io.jutil.jdo.core.parser.EntityMetadata;
import io.jutil.jdo.internal.core.sql.SqlHandler;
import io.jutil.jdo.internal.core.sql.SqlRequest;
import io.jutil.jdo.internal.core.sql.SqlResponse;
import io.jutil.jdo.internal.core.sql.parameter.ParameterSqlHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.List;

/**
 * @author Jin Zheng
 * @since 2022-04-20
 */
public class ParameterSqlHandlerTest {

	private SqlResponse response = new SqlResponse(null);
	private SqlRequest request;
	private SqlHandler handler = new ParameterSqlHandler();

	public ParameterSqlHandlerTest() {
		this.request = Mockito.mock(SqlRequest.class);
		var metadata = Mockito.mock(EntityMetadata.class);
		Mockito.when(metadata.getFieldMap()).thenReturn(new HashMap<>());
		Mockito.when(request.getMetadata()).thenReturn(metadata);
	}

    @Test
    public void testHandle1() {
		response.putParam("k1", "v1");
		response.putParam("k2", "v2");
		response.addName("k2");
		response.addName("k1");

		handler.handle(request, response);

		var paramList = response.toParamList();
	    Assertions.assertEquals(List.of("v2", "v1"), paramList);
    }

	@Test
	public void testHandle2() {
		response.putParam("k1", "v1");
		response.putParam("k2", "v2");
		response.addName("k2");

		handler.handle(request, response);

		var paramList = response.toParamList();
		Assertions.assertEquals(List.of("v2"), paramList);
	}

	@Test
	public void testHandle3() {
		response.putParam("k1", "v1");
		response.putParam("k2", "v2");
		response.addName("k3");

		handler.handle(request, response);

		var paramList = response.toParamList();
		Assertions.assertEquals(1, paramList.size());
		Assertions.assertEquals(null, paramList.get(0));
	}
}
