package org.romankukin.bankapi.dao;

import java.sql.SQLException;
import org.romankukin.bankapi.dao.card.SupplierDao;

public interface TransactionalManager {

  <T> T doTransaction(SupplierDao<T> action) throws SQLException;

  void doTransaction(Runnable action) throws SQLException;
}
