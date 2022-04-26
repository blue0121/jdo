package test.jutil.jdo.dialect;

import io.jutil.jdo.internal.core.dialect.DetectDialect;
import io.jutil.jdo.internal.core.dialect.Dialect;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;

/**
 * @author Jin Zheng
 * @since 2022-02-18
 */
public class DetectDialectTest {
	public DetectDialectTest() {
	}

	@ParameterizedTest
	@CsvSource({"jdbc:mysql://localhost:3306/test,io.jutil.jdo.internal.core.dialect.MySQLDialect",
				"jdbc:oracle:thin:@localhost:1521@test,io.jutil.jdo.internal.core.dialect.OracleDialect",
				"jdbc:postgresql://localhost:1534/test,io.jutil.jdo.internal.core.dialect.PostgreSQLDialect",
				"jdbc:hsqldb:memory:test,io.jutil.jdo.internal.core.dialect.HyperSQLDialect",
				"jdbc:test://localhost,"})
    public void testDialect(String url, Class<?> clazz) throws Exception {
		DataSource ds = Mockito.mock(DataSource.class);
		Connection conn = Mockito.mock(Connection.class);
		DatabaseMetaData meta = Mockito.mock(DatabaseMetaData.class);
		Mockito.when(ds.getConnection()).thenReturn(conn);
		Mockito.when(conn.getMetaData()).thenReturn(meta);
		Mockito.when(meta.getURL()).thenReturn(url);

		if (clazz == null) {
			Assertions.assertThrows(UnsupportedOperationException.class, () -> DetectDialect.dialect(ds),
					"不支持数据库方言: test");
		} else {
			Dialect dialect = DetectDialect.dialect(ds);
			Assertions.assertNotNull(dialect);
			Assertions.assertEquals(clazz, dialect.getClass());
		}
    }

}
