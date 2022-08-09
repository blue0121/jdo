package test.jutil.jdo.executor.parameter;

import io.jutil.jdo.internal.core.sql.SqlParameter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
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
		facade.bind(pstmt, List.of(SqlParameter.create(date)));
		Mockito.verify(pstmt).setTimestamp(Mockito.eq(ONE), Mockito.eq(new Timestamp(date.getTime())));

		Mockito.doReturn(new Timestamp(date.getTime())).when(rs).getTimestamp(Mockito.eq(ONE));
		List<Date> list = facade.fetch(rs, Date.class);
		Assertions.assertEquals(ONE, list.size());
		Assertions.assertEquals(date, list.get(ZERO));
	}

	@Test
	public void testLocalDate() throws SQLException {
		var date = LocalDate.now();
		facade.bind(pstmt, List.of(SqlParameter.create(date)));
		Mockito.verify(pstmt).setDate(Mockito.eq(ONE), Mockito.eq(java.sql.Date.valueOf(date)));

		Mockito.doReturn(java.sql.Date.valueOf(date)).when(rs).getDate(Mockito.eq(ONE));
		List<LocalDate> list = facade.fetch(rs, LocalDate.class);
		Assertions.assertEquals(ONE, list.size());
		Assertions.assertEquals(date, list.get(ZERO));
	}

	@Test
	public void testLocalDateTime() throws SQLException {
		var date = LocalDateTime.now();
		facade.bind(pstmt, List.of(SqlParameter.create(date)));
		Mockito.verify(pstmt).setTimestamp(Mockito.eq(ONE), Mockito.eq(Timestamp.valueOf(date)));

		Mockito.doReturn(Timestamp.valueOf(date)).when(rs).getTimestamp(Mockito.eq(ONE));
		List<LocalDateTime> list = facade.fetch(rs, LocalDateTime.class);
		Assertions.assertEquals(ONE, list.size());
		Assertions.assertEquals(date, list.get(ZERO));
	}

	@Test
	public void testLocalTime() throws SQLException {
		var date = LocalTime.now();
		facade.bind(pstmt, List.of(SqlParameter.create(date)));
		Mockito.verify(pstmt).setTime(Mockito.eq(ONE), Mockito.eq(Time.valueOf(date)));

		Mockito.doReturn(Time.valueOf(date)).when(rs).getTime(Mockito.eq(ONE));
		List<LocalTime> list = facade.fetch(rs, LocalTime.class);
		Assertions.assertEquals(ONE, list.size());
		Assertions.assertEquals(date.truncatedTo(ChronoUnit.SECONDS), list.get(ZERO));
	}

	@Test
	public void testInstant() throws SQLException {
		var date = Instant.now();
		facade.bind(pstmt, List.of(SqlParameter.create(date)));
		Mockito.verify(pstmt).setTimestamp(Mockito.eq(ONE), Mockito.eq(Timestamp.from(date)));

		Mockito.doReturn(Timestamp.from(date)).when(rs).getTimestamp(Mockito.eq(ONE));
		List<Instant> list = facade.fetch(rs, Instant.class);
		Assertions.assertEquals(ONE, list.size());
		Assertions.assertEquals(date, list.get(ZERO));
	}

	@Test
	public void testSqlDate() throws SQLException {
		var date = new java.sql.Date(System.currentTimeMillis());
		facade.bind(pstmt, List.of(SqlParameter.create(date)));
		Mockito.verify(pstmt).setDate(Mockito.eq(1), Mockito.eq(date));

		Mockito.doReturn(date).when(rs).getDate(Mockito.eq(ONE));
		List<java.sql.Date> list = facade.fetch(rs, java.sql.Date.class);
		Assertions.assertEquals(ONE, list.size());
		Assertions.assertEquals(date, list.get(ZERO));
	}

	@Test
	public void testSqlTime() throws SQLException {
		var date = Time.valueOf(LocalTime.now());
		facade.bind(pstmt, List.of(SqlParameter.create(date)));
		Mockito.verify(pstmt).setTime(Mockito.eq(ONE), Mockito.eq(date));

		Mockito.doReturn(date).when(rs).getTime(Mockito.eq(ONE));
		List<Time> list = facade.fetch(rs, Time.class);
		Assertions.assertEquals(ONE, list.size());
		Assertions.assertEquals(date, list.get(ZERO));
	}

	@Test
	public void testSqlTimestamp() throws SQLException {
		var date = Timestamp.valueOf(LocalDateTime.now());
		facade.bind(pstmt, List.of(SqlParameter.create(date)));
		Mockito.verify(pstmt).setTimestamp(Mockito.eq(ONE), Mockito.eq(date));

		Mockito.doReturn(date).when(rs).getTimestamp(Mockito.eq(ONE));
		List<Timestamp> list = facade.fetch(rs, Timestamp.class);
		Assertions.assertEquals(ONE, list.size());
		Assertions.assertEquals(date, list.get(ZERO));
	}

}
