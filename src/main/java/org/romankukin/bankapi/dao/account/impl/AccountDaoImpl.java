package org.romankukin.bankapi.dao.account.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.romankukin.bankapi.dao.account.AccountDao;
import org.romankukin.bankapi.dao.card.impl.CardDaoImpl;
import org.romankukin.bankapi.dto.account.AccountCreateRequest;
import org.romankukin.bankapi.exception.DatabaseQueryException;
import org.romankukin.bankapi.exception.NoSuchEntityInDatabaseException;
import org.romankukin.bankapi.model.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AccountDaoImpl implements AccountDao {
  private static final Logger logger = LoggerFactory.getLogger(CardDaoImpl.class);

  private final static String CREATE_ACCOUNT = "insert into account(number, balance, clientId) values (?, ?, (select id from client where client.phone = ?))";
  private final static String GET_ALL_FROM_ACCOUNT = "select * from account";
  private final static String FIND_CARD = "select * from account where id = '%s'";
  private static final String NO_SUCH_ACCOUNT = "No account with this number in database. Or it is already closed.";

  private final DataSource dataSource;

  public AccountDaoImpl(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public Optional<AccountCreateRequest> createAccount(Connection connection, AccountCreateRequest accountCreateRequest) {
    try (PreparedStatement preparedStatement = connection.prepareStatement(CREATE_ACCOUNT)) {
      preparedStatement.setString(1, accountCreateRequest.getNumber());
      preparedStatement.setBigDecimal(2, accountCreateRequest.getBalance());
      preparedStatement.setString(3, accountCreateRequest.getPhone());
      preparedStatement.executeUpdate();
      return Optional.of(accountCreateRequest);
    } catch (SQLException e) {
      logger.error(e.getMessage());
      throw new DatabaseQueryException();
    }
  }

    @Override
    public Optional<Account> getAccount(int numberId) throws NoSuchEntityInDatabaseException {
    try (Connection connection = dataSource.getConnection()) {
      try (Statement statement = connection.createStatement()) {
        try (ResultSet resultSet = statement.executeQuery(String.format(FIND_CARD, numberId))) {
          if (!resultSet.next()) {
            throw new NoSuchEntityInDatabaseException(NO_SUCH_ACCOUNT);
          }

          return Optional.of(extractAccountFromResultSet(resultSet));
        }
      }
    } catch (SQLException e) {
      logger.error(e.getMessage());
      throw new DatabaseQueryException();
    }
  }

  @Override
  public List<Account> getAllAccounts() throws NoSuchEntityInDatabaseException {
    try (Connection connection = dataSource.getConnection()) {
      try (Statement statement = connection.createStatement()) {
        try (ResultSet resultSet = statement.executeQuery(GET_ALL_FROM_ACCOUNT)) {
          List<Account> accounts = new ArrayList<>();
          while (resultSet.next()) {
            accounts.add(extractAccountFromResultSet(resultSet));
          }
          if (accounts.isEmpty()) {
            throw new NoSuchEntityInDatabaseException(NO_SUCH_ACCOUNT);
          }

          return accounts;
        }
      }
    } catch (SQLException e) {
      logger.error(e.getMessage());
      throw new DatabaseQueryException();
    }
  }
}
