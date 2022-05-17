package io.jutil.jdo.internal.core.engine;

import io.jutil.jdo.core.engine.Jdo;
import io.jutil.jdo.core.engine.JdoTemplate;
import io.jutil.jdo.core.engine.TransactionManager;
import io.jutil.jdo.internal.core.executor.DataSourceFactory;

/**
 * @author Jin Zheng
 * @since 2022-02-28
 */
public class DefaultJdo implements Jdo, AutoCloseable {

	private final DataSourceFactory dataSourceFactory;
	private final JdoTemplate jdoTemplate;
	private final TransactionManager transactionManager;

	public DefaultJdo(DefaultJdoBuilder builder) {
		this.dataSourceFactory = builder.getDataSourceFactory();
		this.jdoTemplate = builder.getJdoTemplate();
		this.transactionManager = builder.getTransactionManager();
	}

	@Override
	public JdoTemplate getJdoTemplate() {
		return jdoTemplate;
	}

	@Override
	public TransactionManager getTransactionManager() {
		return transactionManager;
	}

	@Override
	public void close() {
		this.dataSourceFactory.close();
	}
}
