package org.romankukin.bankapi.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class AccountDaoImpl implements AccountDao {

  @Override
  public Optional getEntity(Object id) throws SQLException {
    return Optional.empty();
  }

  @Override
  public List getAllEntities() throws SQLException {
    return null;
  }

  @Override
  public Object update(Object o) throws SQLException {
    return null;
  }

  @Override
  public boolean create(Object o) throws SQLException {
    return false;
  }

  @Override
  public boolean delete(Object o) {
    return false;
  }
}
