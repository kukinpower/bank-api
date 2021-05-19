package org.romankukin.bankapi.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface BankDao<T, K> {
  Optional<T> getEntity(K id) throws SQLException;

  List<T> getAllEntities();

  T update(T t, String[] params);

  boolean create(T t) throws SQLException;

  boolean delete(T t);
}
