package test.jutil.jdo.executor.parameter;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

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
		factory.bind(pstmt, List.of(str));
		Mockito.verify(pstmt).setString(Mockito.eq(1), Mockito.eq(str));
	}

	@Test
	public void testObject() throws SQLException {
		var obj = new Object();
		factory.bind(pstmt, List.of(obj));
		Mockito.verify(pstmt).setObject(Mockito.eq(1), Mockito.eq(obj));
	}

	@Test
	public void testNull() throws SQLException {
		List<Object> list = new ArrayList<>();
		list.add(null);
		factory.bind(pstmt, list);
		Mockito.verify(pstmt).setObject(Mockito.eq(1), Mockito.eq(null));
	}

	@Test
	public void testList() throws SQLException {
		var now = LocalDateTime.now();
		var obj = new Object();
		List<Object> list = new ArrayList<>();
		list.add(null);
		list.add(obj);
		list.add(123);
		list.add(123.0d);
		list.add(now);
		factory.bind(pstmt, list);
		Mockito.verify(pstmt).setObject(Mockito.eq(1), Mockito.eq(null));
		Mockito.verify(pstmt).setObject(Mockito.eq(2), Mockito.eq(obj));
		Mockito.verify(pstmt).setInt(Mockito.eq(3), Mockito.eq(123));
		Mockito.verify(pstmt).setDouble(Mockito.eq(4), Mockito.eq(123.0d));
		Mockito.verify(pstmt).setTimestamp(Mockito.eq(5), Mockito.eq(Timestamp.valueOf(now)));
	}

}
