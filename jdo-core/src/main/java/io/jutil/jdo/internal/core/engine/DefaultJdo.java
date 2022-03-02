package io.jutil.jdo.internal.core.engine;

import io.jutil.jdo.core.engine.Jdo;
import io.jutil.jdo.core.engine.JdoTemplate;
import io.jutil.jdo.internal.core.executor.DataSourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jin Zheng
 * @since 2022-02-28
 */
public class DefaultJdo implements Jdo, AutoCloseable {
	private static Logger logger = LoggerFactory.getLogger(DefaultJdo.class);

	private final DataSourceFactory dataSourceFactory;
	private final JdoTemplate jdoTemplate;

	public DefaultJdo(DefaultJdoBuilder builder) {
		this.dataSourceFactory = builder.getDataSourceFactory();
		this.jdoTemplate = builder.getJdoTemplate();
	}

	@Override
	public JdoTemplate getJdoTemplate() {
		return jdoTemplate;
	}

	@Override
	public void close() {
		this.dataSourceFactory.close();
	}
}
