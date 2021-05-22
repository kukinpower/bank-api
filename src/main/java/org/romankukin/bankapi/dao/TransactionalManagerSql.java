package org.romankukin.bankapi.dao;

import org.romankukin.bankapi.exception.DatabaseQueryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
  private static final String TRANSACTION_ERROR = "Transaction failed {}";
  private static final String CONNECTION_NULL = "Connection is null";

  public TransactionalManagerSql(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public <T> T doTransaction(SupplierDao<T> action) throws TransactionFailedException {
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
    } catch (SQLException | DatabaseQueryException e) {
      logger.error(TRANSACTION_ERROR, e.getMessage());
      if (connection != null) {
        try {
          connection.rollback(savepoint);
        } catch (SQLException ex) {
          logger.error(TRANSACTION_ERROR, e.getMessage());
          throw new TransactionFailedException();
        }
        logger.error(TRANSACTION_ROLLBACK);
      } else {
        logger.error(CONNECTION_NULL);
      }
      throw new TransactionFailedException();
    }
  }
}
