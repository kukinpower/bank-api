package org.romankukin.bankapi.dao.account.impl;

import java.util.List;
import java.util.Optional;
import org.romankukin.bankapi.dao.account.AccountDao;
import org.romankukin.bankapi.model.Account;

public class AccountDaoImpl implements AccountDao {

  @Override
  public Optional<Account> getEntity(int id) {
    return Optional.empty();
  }

  @Override
  public List<Account> getAllEntities() {
    return null;
  }

  @Override
  public Account update(Account t) {
    return null;
  }

  @Override
  public boolean create(Account t) {
    return false;
  }

  @Override
  public boolean delete(Account t) {
    return false;
  }
}
