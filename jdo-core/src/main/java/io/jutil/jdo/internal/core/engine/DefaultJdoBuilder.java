package io.jutil.jdo.internal.core.engine;

import io.jutil.jdo.core.engine.DataSourceOptions;
import io.jutil.jdo.core.engine.Jdo;
import io.jutil.jdo.core.engine.JdoBuilder;
import io.jutil.jdo.core.engine.JdoTemplate;
import io.jutil.jdo.internal.core.dialect.DetectDialect;
import io.jutil.jdo.internal.core.executor.ConnectionFactory;
import io.jutil.jdo.internal.core.executor.DataSourceFactory;
import io.jutil.jdo.internal.core.executor.metadata.TableChecker;
import io.jutil.jdo.internal.core.parser.ParserFacade;
import io.jutil.jdo.internal.core.path.ClassScanner;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jin Zheng
 * @since 2022-02-28
 */
@NoArgsConstructor
public class DefaultJdoBuilder implements JdoBuilder {
	private static Logger logger = LoggerFactory.getLogger(DefaultJdoBuilder.class);

	private DataSourceOptions dataSourceOptions;
	private DataSourceFactory dataSourceFactory;
	private JdoTemplate jdoTemplate;
	private ParserFacade parserFacade;
	private final List<Class<?>> clazzList = new ArrayList<>();
	private final List<String> pkgList = new ArrayList<>();


	@Override
	public Jdo build() {
		this.dataSourceFactory = new DataSourceFactory(dataSourceOptions);
		var dialect = DetectDialect.dialect(dataSourceFactory.getDateSource());
		this.parserFacade = new ParserFacade(dialect, true);
		var connectionFactory = new ConnectionFactory(dataSourceFactory.getDateSource(), parserFacade);
		this.jdoTemplate = new DefaultJdoTemplate(parserFacade, connectionFactory);

		this.parseClazz();
		var tableChecker = new TableChecker(dataSourceFactory.getDateSource(), parserFacade.getMetadataCache());
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
			parserFacade.parse(clazz);
		}
		ClassScanner scanner = new ClassScanner(c -> parserFacade.parse(c));
		scanner.scan(pkgList);
	}

	public DataSourceFactory getDataSourceFactory() {
		return dataSourceFactory;
	}

	public JdoTemplate getJdoTemplate() {
		return jdoTemplate;
	}

}
