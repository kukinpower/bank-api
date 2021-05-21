package org.romankukin.bankapi.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.function.Supplier;
import java.util.logging.Logger;
import javax.sql.DataSource;
import org.romankukin.bankapi.dao.card.SupplierThrows;
import org.romankukin.bankapi.exception.TransactionFailedException;

public class TransactionalManagerSql implements TransactionalManager {

  private final DataSource dataSource;
  private static final Logger logger = Logger.getLogger(TransactionalManagerSql.class.getName());
  private static final String SAVEPOINT_TRANSACTION_START = "Start of transaction";
  private static final String TRANSACTION_START = "Transaction started";
  private static final String TRANSACTION_FINISH = "Transaction finished successful";
  private static final String TRANSACTION_ROLLBACK = "Transaction: rollback to save point";
  private static final String WARN_TRANSACTION = "Warning: Transaction ";
  private static final String CONNECTION_NULL = "Connection is null";

  public TransactionalManagerSql(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public <T> T doTransaction(SupplierThrows<T> action) throws SQLException {
    Connection connection = null;
    Savepoint savepoint = null;
    try {
      connection = dataSource.getConnection();
      connection.setAutoCommit(false);
      savepoint = connection.setSavepoint(SAVEPOINT_TRANSACTION_START);
      logger.info(TRANSACTION_START);
      T res = action.get(connection);
      connection.commit();
      logger.info(TRANSACTION_FINISH);
      return res;
    } catch (SQLException e) {
      logger.warning(WARN_TRANSACTION + e.getMessage());
      if (connection != null) {
        connection.rollback(savepoint);
        logger.info(TRANSACTION_ROLLBACK);
      } else {
        logger.warning(CONNECTION_NULL);
      }
      throw new TransactionFailedException(e);
    }
  }

  @Override
  public void doTransaction(Runnable action) throws SQLException {
    Connection connection = null;
    Savepoint savepoint = null;
    try {
      connection = dataSource.getConnection();
      connection.setAutoCommit(false);
      savepoint = connection.setSavepoint(SAVEPOINT_TRANSACTION_START);
      logger.info(TRANSACTION_START);
      action.run();
      connection.commit();
      logger.info(TRANSACTION_FINISH);
    } catch (SQLException e) {
      logger.warning(WARN_TRANSACTION + e.getMessage());
      if (connection != null) {
        connection.rollback(savepoint);
        logger.info(TRANSACTION_ROLLBACK);
      } else {
        logger.warning(CONNECTION_NULL);
      }
      throw new TransactionFailedException(e);
    }
  }
}
