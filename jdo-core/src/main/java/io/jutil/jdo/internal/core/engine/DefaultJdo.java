package io.jutil.jdo.internal.core.engine;

import com.zaxxer.hikari.HikariDataSource;
import io.jutil.jdo.core.engine.Jdo;
import io.jutil.jdo.core.engine.JdoTemplate;
import io.jutil.jdo.core.engine.TransactionManager;

import javax.sql.DataSource;

/**
 * @author Jin Zheng
 * @since 2022-02-28
 */
public class DefaultJdo implements Jdo, AutoCloseable {
	private final DataSource dataSource;
	private final JdoTemplate jdoTemplate;
	private final TransactionManager transactionManager;

	public DefaultJdo(DefaultJdoBuilder builder) {
		this.dataSource = builder.getDataSource();
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
		if (dataSource instanceof HikariDataSource ds) {
			if (!ds.isClosed()) {
				ds.close();
			}
		}
	}
}
