package test.jutil.jdo.sql;

import io.jutil.jdo.internal.core.dialect.Dialect;
import io.jutil.jdo.internal.core.dialect.MySQLDialect;
import io.jutil.jdo.internal.core.parser.EntityConfigCache;
import io.jutil.jdo.internal.core.parser.MapperConfigCache;
import io.jutil.jdo.internal.core.parser.ParserFactory;
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
	protected final EntityConfigCache entityConfigCache;
	protected final MapperConfigCache mapperConfigCache;
	protected final SqlHandlerFactory factory;

	public SqlHandlerTest() {
		this.dialect = new MySQLDialect();
		this.parseFactory = new ParserFactory(dialect);
		this.entityConfigCache = parseFactory.getEntityConfigCache();
		this.mapperConfigCache = parseFactory.getMapperConfigCache();
		this.factory = new SqlHandlerFactory();

		parseFactory.parse(GroupEntity.class);
		parseFactory.parse(GroupMapper.class);
		parseFactory.parse(ResultMapper.class);
		parseFactory.parse(UserEntity.class);
	}

}
