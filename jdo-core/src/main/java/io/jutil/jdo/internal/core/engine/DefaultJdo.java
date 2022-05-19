package io.jutil.jdo.internal.core.engine;

import com.zaxxer.hikari.HikariDataSource;
import io.jutil.jdo.core.engine.Jdo;
import io.jutil.jdo.core.engine.JdoTemplate;
import io.jutil.jdo.core.engine.TransactionManager;

/**
 * @author Jin Zheng
 * @since 2022-02-28
 */
public class DefaultJdo implements Jdo, AutoCloseable {
	private final DefaultJdoBuilder jdoBuilder;

	public DefaultJdo(DefaultJdoBuilder builder) {
		this.jdoBuilder = builder;
	}

	@Override
	public JdoTemplate getJdoTemplate() {
		return jdoBuilder.getJdoTemplate();
	}

	@Override
	public TransactionManager getTransactionManager() {
		return jdoBuilder.getTransactionManager();
	}

	@Override
	public void close() {
		var dataSource = jdoBuilder.getDataSource();
		if (dataSource instanceof HikariDataSource ds) {
			if (!ds.isClosed()) {
				ds.close();
			}
		}
	}
}
