package io.jutil.jdo.internal.core.engine;

import io.jutil.jdo.core.engine.DataSourceOptions;
import io.jutil.jdo.core.engine.Jdo;
import io.jutil.jdo.core.engine.JdoBuilder;
import io.jutil.jdo.core.engine.JdoTemplate;
import io.jutil.jdo.internal.core.dialect.DetectDialect;
import io.jutil.jdo.internal.core.executor.ConnectionFactory;
import io.jutil.jdo.internal.core.executor.DataSourceFactory;
import io.jutil.jdo.internal.core.executor.mapper.RowMapperFactory;
import io.jutil.jdo.internal.core.executor.metadata.TableChecker;
import io.jutil.jdo.internal.core.parser.ParserFactory;
import io.jutil.jdo.internal.core.path.ClassScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jin Zheng
 * @since 2022-02-28
 */
public class DefaultJdoBuilder implements JdoBuilder {
	private static Logger logger = LoggerFactory.getLogger(DefaultJdoBuilder.class);

	private DataSourceOptions dataSourceOptions;
	private DataSourceFactory dataSourceFactory;
	private JdoTemplate jdoTemplate;
	private ParserFactory parserFactory;
	private final List<Class<?>> clazzList = new ArrayList<>();
	private final List<String> pkgList = new ArrayList<>();

	public DefaultJdoBuilder() {
	}

	@Override
	public Jdo build() {
		this.dataSourceFactory = new DataSourceFactory(dataSourceOptions);
		var dialect = DetectDialect.dialect(dataSourceFactory.getDateSource());
		this.parserFactory = new ParserFactory(dialect, true);
		var rowMapperFactory = new RowMapperFactory(parserFactory);
		var connectionFactory = new ConnectionFactory(dataSourceFactory.getDateSource(), rowMapperFactory);
		this.jdoTemplate = new DefaultJdoTemplate(parserFactory, connectionFactory);

		this.parseClazz();
		var tableChecker = new TableChecker(dataSourceFactory.getDateSource(), parserFactory.getConfigCache());
		tableChecker.check();

		DefaultJdo jdo = new DefaultJdo(this);
		return jdo;
	}

	@Override
	public JdoBuilder setDataSourceOptions(DataSourceOptions options) {
		this.dataSourceOptions = options;
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

	private void parseClazz() {
		for (var clazz : clazzList) {
			parserFactory.parse(clazz);
		}
		ClassScanner scanner = new ClassScanner(c -> parserFactory.parse(c));
		scanner.scan(pkgList);
	}

	public DataSourceFactory getDataSourceFactory() {
		return dataSourceFactory;
	}

	public JdoTemplate getJdoTemplate() {
		return jdoTemplate;
	}

}
