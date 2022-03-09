package test.jutil.jdo.executor.parameter;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

/**
 * @author Jin Zheng
 * @since 2022-03-09
 */
public class DateBinderTest extends BaseBinderTest {
	public DateBinderTest() {
	}

	@Test
	public void testDate() throws SQLException {
		var date = new Date();
		factory.bind(pstmt, List.of(date));
		Mockito.verify(pstmt).setTimestamp(Mockito.eq(1), Mockito.eq(new Timestamp(date.getTime())));
	}

	@Test
	public void testLocalDate() throws SQLException {
		var date = LocalDate.now();
		factory.bind(pstmt, List.of(date));
		Mockito.verify(pstmt).setDate(Mockito.eq(1), Mockito.eq(java.sql.Date.valueOf(date)));
	}

	@Test
	public void testLocalDateTime() throws SQLException {
		var date = LocalDateTime.now();
		factory.bind(pstmt, List.of(date));
		Mockito.verify(pstmt).setTimestamp(Mockito.eq(1), Mockito.eq(Timestamp.valueOf(date)));
	}

	@Test
	public void testLocalTime() throws SQLException {
		var date = LocalTime.now();
		factory.bind(pstmt, List.of(date));
		Mockito.verify(pstmt).setTime(Mockito.eq(1), Mockito.eq(Time.valueOf(date)));
	}

	@Test
	public void testSqlDate() throws SQLException {
		var date = new java.sql.Date(System.currentTimeMillis());
		factory.bind(pstmt, List.of(date));
		Mockito.verify(pstmt).setDate(Mockito.eq(1), Mockito.eq(date));
	}

	@Test
	public void testSqlTime() throws SQLException {
		var date = Time.valueOf(LocalTime.now());
		factory.bind(pstmt, List.of(date));
		Mockito.verify(pstmt).setTime(Mockito.eq(1), Mockito.eq(date));
	}

	@Test
	public void testSqlTimestamp() throws SQLException {
		var date = Timestamp.valueOf(LocalDateTime.now());
		factory.bind(pstmt, List.of(date));
		Mockito.verify(pstmt).setTimestamp(Mockito.eq(1), Mockito.eq(date));
	}

}
