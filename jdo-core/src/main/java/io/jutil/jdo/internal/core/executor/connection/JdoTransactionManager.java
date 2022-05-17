package io.jutil.jdo.internal.core.executor.connection;

import io.jutil.jdo.core.engine.TransactionManager;
import io.jutil.jdo.core.exception.JdbcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

/**
 * @author Jin Zheng
 * @since 2022-05-17
 */
public class JdoTransactionManager implements TransactionManager {
    private static Logger logger = LoggerFactory.getLogger(JdoTransactionManager.class);

    private final JdoConnectionHolder connectionHolder;

    public JdoTransactionManager(JdoConnectionHolder connectionHolder) {
        this.connectionHolder = connectionHolder;
    }

    @Override
    public void begin() {
        connectionHolder.setAutoCommit(false);
        logger.debug("开始事务");
    }

    @Override
    public void commit() {
        try {
            connectionHolder.getConnection().commit();
            logger.debug("事务提交");
        } catch (SQLException e) {
            throw new JdbcException(e);
        }
    }

    @Override
    public void rollback() {
        try {
            connectionHolder.getConnection().rollback();
            logger.debug("事务回滚");
        } catch (SQLException e) {
            throw new JdbcException(e);
        }
    }
}
