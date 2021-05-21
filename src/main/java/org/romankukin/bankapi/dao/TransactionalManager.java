package org.romankukin.bankapi.dao;

import java.sql.SQLException;
import java.util.function.Supplier;

public interface TransactionalManager {

  <T> T doTransaction(Supplier<T> action) throws SQLException;

  void doTransaction(Runnable action) throws SQLException;
}
