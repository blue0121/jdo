package test.jutil.jdo.sql;

import io.jutil.jdo.internal.core.dialect.Dialect;
import io.jutil.jdo.internal.core.dialect.MySQLDialect;
import io.jutil.jdo.internal.core.parser2.MetadataCache;
import io.jutil.jdo.internal.core.parser2.ParserFacade;
import io.jutil.jdo.internal.core.sql.SqlHandlerFacade;
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
	protected final ParserFacade parserFacade;
	protected final MetadataCache metadataCache;
	protected final SqlHandlerFacade facade;

	public SqlHandlerTest() {
		this.dialect = new MySQLDialect();
		this.parserFacade = new ParserFacade(dialect, true);
		this.metadataCache = parserFacade.getMetadataCache();
		this.facade = new SqlHandlerFacade();

		parserFacade.parse(GroupEntity.class);
		parserFacade.parse(GroupMapper.class);
		parserFacade.parse(ResultMapper.class);
		parserFacade.parse(UserEntity.class);
	}

}
