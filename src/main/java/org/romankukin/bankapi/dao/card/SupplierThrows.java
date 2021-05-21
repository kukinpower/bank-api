package org.romankukin.bankapi.dao.card;

import java.sql.Connection;
import java.sql.SQLException;

@FunctionalInterface
public interface SupplierThrows<T> {

  /**
   * Gets a result.
   *
   * @return a result
   */
  T get(Connection connection);
}
