package org.romankukin.bankapi.dao.account;

import java.util.List;
import java.util.Optional;
import org.romankukin.bankapi.model.Account;

public interface AccountDao {
  Optional<Account> getEntity(int id);

  List<Account> getAllEntities();

  Account update(Account t);

  boolean create(Account t);

  boolean delete(Account t);
}
