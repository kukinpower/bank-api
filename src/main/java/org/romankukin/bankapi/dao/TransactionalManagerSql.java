package org.romankukin.bankapi.dao;

import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;
import javax.sql.DataSource;
import org.romankukin.bankapi.dao.card.SupplierDao;
import org.romankukin.bankapi.exception.TransactionFailedException;

public class TransactionalManagerSql implements TransactionalManager {

  private final DataSource dataSource;
  private static final Logger logger = LoggerFactory.getLogger(TransactionalManagerSql.class);
  private static final String SAVEPOINT_TRANSACTION_START = "Start of transaction";
  private static final String TRANSACTION_START = "Transaction started";
  private static final String TRANSACTION_FINISH = "Transaction finished successful";
  private static final String TRANSACTION_ROLLBACK = "Transaction: rollback to save point";
  private static final String WARN_TRANSACTION = "Warning: Transaction ";
  private static final String CONNECTION_NULL = "Connection is null";

  public TransactionalManagerSql(DataSource dataSource) {
    this.dataSource = dataSource;
  }
//
//  @Override
//  public <T> T doTransaction(SupplierDao<T> action) throws SQLException {
//    Connection connection = null;
//    Savepoint savepoint = null;
//    try {
//      connection = dataSource.getConnection();
//      connection.setAutoCommit(false);
//      savepoint = connection.setSavepoint(SAVEPOINT_TRANSACTION_START);
//      logger.info(TRANSACTION_START);
//      T res = action.get(connection);
//      connection.commit();
//      logger.info(TRANSACTION_FINISH);
//      return res;
//    } catch (SQLException e) {
//      logger.warning(WARN_TRANSACTION + e.getMessage());
//      if (connection != null) {
//        connection.rollback(savepoint);
//        logger.info(TRANSACTION_ROLLBACK);
//      } else {
//        logger.warning(CONNECTION_NULL);
//      }
//      throw new TransactionFailedException(e);
//    }
//  }
//
  @Override
  public <T> T doTransaction(SupplierDao<T> action) throws SQLException {
    Connection connection = null;
    Savepoint savepoint = null;
    try {
      connection = dataSource.getConnection();
      connection.setAutoCommit(false);
      savepoint = connection.setSavepoint(SAVEPOINT_TRANSACTION_START);
      logger.debug(TRANSACTION_START);
      T res = action.execute(connection);
      connection.commit();
      logger.debug(TRANSACTION_FINISH);
      return res;
    } catch (SQLException e) {
      logger.error(WARN_TRANSACTION + e.getMessage());
      if (connection != null) {
        connection.rollback(savepoint);
        logger.error(TRANSACTION_ROLLBACK);
      } else {
        logger.error(CONNECTION_NULL);
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
      logger.debug(TRANSACTION_START);
      action.run();
      connection.commit();
      logger.debug(TRANSACTION_FINISH);
    } catch (SQLException e) {
      logger.error(WARN_TRANSACTION + e.getMessage());
      if (connection != null) {
        connection.rollback(savepoint);
        logger.error(TRANSACTION_ROLLBACK);
      } else {
        logger.error(CONNECTION_NULL);
      }
      throw new TransactionFailedException(e);
    }
  }
}