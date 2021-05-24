package org.romankukin.bankapi.dao.account;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import org.romankukin.bankapi.dto.account.AccountCreateRequest;
import org.romankukin.bankapi.exception.NoSuchEntityInDatabaseException;
import org.romankukin.bankapi.model.Account;

public interface AccountDao {

  default Account extractAccountFromResultSet(ResultSet resultSet) throws SQLException {
    String number = resultSet.getString("number");
    String balance = resultSet.getString("balance");
    int clientId = resultSet.getInt("clientId");
    return new Account(number, balance, clientId);
  }

  Optional<AccountCreateRequest> createAccount(Connection connection,
      AccountCreateRequest accountCreateRequest);

  List<Account> getAllAccounts() throws NoSuchEntityInDatabaseException;

  Optional<Account> getAccount(int numberId) throws NoSuchEntityInDatabaseException;
}
