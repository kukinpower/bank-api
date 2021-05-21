package org.romankukin.bankapi.dao.card;

import java.sql.Connection;
import java.sql.SQLException;

@FunctionalInterface
public interface SupplierDao<T> {

  T execute(Connection connection);
}
