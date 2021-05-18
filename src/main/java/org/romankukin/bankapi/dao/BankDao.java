package org.romankukin.bankapi.dao;

import java.util.List;
import java.util.Optional;

public interface BankDao<T, K> {
  Optional<T> getEntity(K id);

  List<T> getAllEntities();

  T update(T t, String[] params);

  boolean create(T t);

  boolean delete(T t);
}
