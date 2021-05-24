package org.romankukin.bankapi.dao.card;

import java.sql.Connection;

@FunctionalInterface
public interface SupplierDao<T> {

  T execute(Connection connection);
}
