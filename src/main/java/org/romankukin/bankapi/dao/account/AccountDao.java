package org.romankukin.bankapi.dao.account;

import java.sql.Connection;
import java.util.Optional;
import org.romankukin.bankapi.dto.account.AccountCreateRequest;

public interface AccountDao {
  Optional<AccountCreateRequest> createAccount(Connection connection, AccountCreateRequest accountCreateRequest);
}
