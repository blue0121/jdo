package test.jutil.jdo.it;

import io.jutil.jdo.core.engine.DataSourceOptions;
import io.jutil.jdo.core.engine.Jdo;
import io.jutil.jdo.core.engine.JdoBuilder;
import io.jutil.jdo.core.engine.JdoTemplate;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

/**
 * @author Jin Zheng
 * @since 2022-03-02
 */
public abstract class BaseTest {
	protected static Jdo jdo;
    protected static JdoTemplate jdoTemplate;

	public BaseTest() {
	}

	@BeforeAll
	public static void beforeAll() {
		DataSourceOptions dsOptions = new DataSourceOptions();
		dsOptions.setUrl("jdbc:h2:mem:testDB");
		dsOptions.setUsername("sa");
		jdo = JdoBuilder.create()
				.setDataSource(dsOptions.getDataSource())
				.addScanPackages("test.jutil.jdo.model")
				.setInitSql(initSql())
				.build();
		jdoTemplate = jdo.getJdoTemplate();
	}

	private static String initSql() {
		return """
                create table if not exists usr_group
				(
					id int4 primary key,
					name varchar(20) not null,
					count integer default 0 not null
				);
				create table if not exists usr_user
				(
					id int4 auto_increment primary key,
					group_id int4,
					version int4 default 0 not null,
					name varchar(20) not null,
					password varchar(50) not null,
					state varchar(20) default 'ACTIVE' not null
				)
				""";
	}

	protected void beforeEach() {
		jdoTemplate.execute("insert into usr_group (id,name,count) values (1,'blue',1)");
		jdoTemplate.execute("insert into usr_group (id,name,count) values (2,'green',1)");
	}

	protected void afterEach() {
		jdoTemplate.execute("truncate table usr_group");
		jdoTemplate.execute("truncate table usr_user");
	}

	@AfterAll
	public static void afterAll() {
		if (jdo != null) {
			jdo.close();
		}
	}

}
