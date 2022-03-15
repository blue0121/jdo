package test.jutil.jdo.executor.parameter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import test.jutil.jdo.model.State;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Jin Zheng
 * @since 2022-03-09
 */
public class ObjectBinderTest extends BaseBinderTest {

	public ObjectBinderTest() {
	}

	@Test
	public void testString() throws SQLException {
		var str = "123";
		facade.bind(pstmt, List.of(str));
		Mockito.verify(pstmt).setString(Mockito.eq(ONE), Mockito.eq(str));

		Mockito.doReturn(str).when(rs).getString(Mockito.eq(ONE));
		List<String> list = facade.fetch(rs, String.class);
		Assertions.assertEquals(ONE, list.size());
		Assertions.assertEquals(str, list.get(ZERO));
	}

	@Test
	public void testEnum() throws SQLException {
		var state = State.ACTIVE;
		facade.bind(pstmt, List.of(state));
		Mockito.verify(pstmt).setString(Mockito.eq(ONE), Mockito.eq(state.name()));

		Mockito.doReturn(state.name()).when(rs).getString(Mockito.eq(ONE));
		List<State> list = facade.fetch(rs, State.class);
		Assertions.assertEquals(ONE, list.size());
		Assertions.assertEquals(state, list.get(ZERO));
	}

	@Test
	public void testNull() throws SQLException {
		List<Object> list = new ArrayList<>();
		list.add(null);
		facade.bind(pstmt, list);
		Mockito.verify(pstmt).setObject(Mockito.eq(ONE), Mockito.eq(null));

		Mockito.doReturn(null).when(rs).getString(Mockito.eq(ONE));
		List<State> list2 = facade.fetch(rs, State.class);
		Assertions.assertEquals(ZERO, list2.size());
	}

	@Test
	public void testList() throws SQLException {
		var now = LocalDateTime.now();
		List<Object> list = new ArrayList<>();
		list.add(null);
		list.add(123);
		list.add(123.0d);
		list.add(now);
		facade.bind(pstmt, list);
		Mockito.verify(pstmt).setObject(Mockito.eq(1), Mockito.eq(null));
		Mockito.verify(pstmt).setInt(Mockito.eq(2), Mockito.eq(123));
		Mockito.verify(pstmt).setDouble(Mockito.eq(3), Mockito.eq(123.0d));
		Mockito.verify(pstmt).setTimestamp(Mockito.eq(4), Mockito.eq(Timestamp.valueOf(now)));

		Mockito.doReturn(null).when(rs).getString(Mockito.eq(ONE));
		List<State> list2 = facade.fetch(rs, State.class);
		Assertions.assertEquals(ZERO, list2.size());
	}

}
