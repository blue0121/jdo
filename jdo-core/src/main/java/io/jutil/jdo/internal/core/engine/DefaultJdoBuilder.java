package io.jutil.jdo.internal.core.engine;

import io.jutil.jdo.core.engine.Jdo;
import io.jutil.jdo.core.engine.JdoBuilder;
import io.jutil.jdo.core.engine.JdoTemplate;
import io.jutil.jdo.core.engine.TransactionManager;
import io.jutil.jdo.core.plugin.ConnectionHolder;
import io.jutil.jdo.internal.core.dialect.DetectDialect;
import io.jutil.jdo.internal.core.executor.SqlExecutor;
import io.jutil.jdo.internal.core.executor.metadata.TableChecker;
import io.jutil.jdo.internal.core.parser.ParserFacade;
import io.jutil.jdo.internal.core.path.ClassScanner;
import io.jutil.jdo.internal.core.plugin.JdoConnectionHolder;
import io.jutil.jdo.internal.core.plugin.JdoTransactionManager;
import io.jutil.jdo.internal.core.util.AssertUtil;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Jin Zheng
 * @since 2022-02-28
 */
@NoArgsConstructor
public class DefaultJdoBuilder implements JdoBuilder {
	private static Logger logger = LoggerFactory.getLogger(DefaultJdoBuilder.class);

	private String sql;
	private DataSource dataSource;
	private JdoTemplate jdoTemplate;
	private ConnectionHolder connectionHolder;
	private TransactionManager transactionManager;
	private ParserFacade parserFacade;
	private final List<Class<?>> clazzList = new ArrayList<>();
	private final List<String> pkgList = new ArrayList<>();


	@Override
	public Jdo build() {
		AssertUtil.notNull(dataSource, "数据源");

		var dialect = DetectDialect.dialect(dataSource);
		this.parserFacade = new ParserFacade(dialect);

		this.initConnectionHolder(dataSource);
		var sqlExecutor = new SqlExecutor(connectionHolder, parserFacade);
		this.jdoTemplate = new DefaultJdoTemplate(parserFacade, sqlExecutor);

		this.executeSql();

		this.parseClazz();
		var tableChecker = new TableChecker(dataSource, parserFacade.getMetadataCache());
		tableChecker.check();

		DefaultJdo jdo = new DefaultJdo(this);
		return jdo;
	}

	@Override
	public JdoBuilder setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
		return this;
	}

	@Override
	public JdoBuilder addClazz(Class<?>... clazzes) {
		for (var clazz : clazzes) {
			if (!clazzList.contains(clazz)) {
				clazzList.add(clazz);
			}
		}
		return this;
	}

	@Override
	public JdoBuilder addScanPackages(String... pkgs) {
		for (var pkg : pkgs) {
			if (!pkgList.contains(pkg)) {
				pkgList.add(pkg);
			}
		}
		return this;
	}

	@Override
	public JdoBuilder setConnectionHolder(ConnectionHolder holder) {
		this.connectionHolder = holder;
		return this;
	}

	@Override
	public JdoBuilder setInitSql(String sql) {
		this.sql = sql;
		return this;
	}

	private void executeSql() {
		if (sql == null || sql.isEmpty()) {
			return;
		}

		if (logger.isDebugEnabled()) {
			logger.info("使用 SQL 初始化数据库");
		}
		this.jdoTemplate.execute(sql);
	}

	private void initConnectionHolder(DataSource dataSource) {
		if (connectionHolder != null) {
			return;
		}

		var holder = new JdoConnectionHolder(dataSource);
		this.connectionHolder = holder;
		this.transactionManager = new JdoTransactionManager(holder);
	}

	private void parseClazz() {
		for (var clazz : clazzList) {
			parserFacade.parse(clazz);
		}
		ClassScanner scanner = new ClassScanner(c -> parserFacade.parse(c));
		scanner.scan(pkgList);
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public JdoTemplate getJdoTemplate() {
		return jdoTemplate;
	}

	public TransactionManager getTransactionManager() {
		if (transactionManager == null) {
			throw new UnsupportedOperationException("不支持该事务管理器");
		}

		return transactionManager;
	}

}
