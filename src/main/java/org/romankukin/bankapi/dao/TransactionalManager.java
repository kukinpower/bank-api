package org.romankukin.bankapi.dao;

import java.sql.SQLException;
import java.util.function.Supplier;
import org.romankukin.bankapi.dao.card.SupplierThrows;

public interface TransactionalManager {

  <T> T doTransaction(SupplierThrows<T> action) throws SQLException;

  void doTransaction(Runnable action) throws SQLException;
}
