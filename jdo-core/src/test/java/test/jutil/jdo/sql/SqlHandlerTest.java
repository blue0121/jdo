package test.jutil.jdo.sql;

import io.jutil.jdo.internal.core.dialect.Dialect;
import io.jutil.jdo.internal.core.dialect.MySQLDialect;
import io.jutil.jdo.internal.core.parser.ConfigCache;
import io.jutil.jdo.internal.core.parser.ParserFactory;
import io.jutil.jdo.internal.core.sql.SqlHandlerFacade;
import io.jutil.jdo.internal.core.sql.SqlHandlerFactory;
import test.jutil.jdo.model.GroupEntity;
import test.jutil.jdo.model.GroupMapper;
import test.jutil.jdo.model.ResultMapper;
import test.jutil.jdo.model.UserEntity;

/**
 * @author Jin Zheng
 * @since 2022-02-21
 */
public abstract class SqlHandlerTest {
	protected final Dialect dialect;
	protected final ParserFactory parseFactory;
	protected final ConfigCache configCache;
	protected final SqlHandlerFactory factory;
	protected final SqlHandlerFacade facade;

	public SqlHandlerTest() {
		this.dialect = new MySQLDialect();
		this.parseFactory = new ParserFactory(dialect, true);
		this.configCache = parseFactory.getConfigCache();
		this.factory = new SqlHandlerFactory();
		this.facade = new SqlHandlerFacade();

		parseFactory.parse(GroupEntity.class);
		parseFactory.parse(GroupMapper.class);
		parseFactory.parse(ResultMapper.class);
		parseFactory.parse(UserEntity.class);
	}

}
