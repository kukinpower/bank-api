package org.romankukin.bankapi.dao.account.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;
import javax.sql.DataSource;
import org.romankukin.bankapi.dao.account.AccountDao;
import org.romankukin.bankapi.dao.card.impl.CardDaoImpl;
import org.romankukin.bankapi.dto.account.AccountCreateRequest;
import org.romankukin.bankapi.exception.DatabaseQueryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AccountDaoImpl implements AccountDao {
  private static final Logger logger = LoggerFactory.getLogger(CardDaoImpl.class);

  private final static String CREATE_ACCOUNT = "insert into account(number, clientId) values (?, (select id from client where client.phone = ?))";
  private final static String GET_ALL_FROM_CARD = "select * from card";
  private final static String FIND_CARD = "select * from card where number = '%s'";
  private final static String GET_CARD_BALANCE = "select balance from card where number = '%s'";
  private final static String GET_CARD_STATUS = "select status from card where number = '%s'";
  private static final String DELETE_CARD = "delete from card where number = ?";
  private static final String UPDATE_CARD = "update card set balance = ?, status = ? where number = ? and status != 3";
  private static final String UPDATE_CARD_STATUS = "update card set status = ? where number = ? and status != 3";
  private static final String UPDATE_CARD_BALANCE = "update card set status = case when status = 1 then 2 else status end,"
      + " balance = balance + ? where number = ? and status != 3";
  private static final String GET_ALL_STATUS = "select number, descriptor from CARD join status s on s.id = card.status group by descriptor, number";

  private static final String NO_SUCH_CARD = "No card with this number in database. Or it is already closed.";

  private final DataSource dataSource;

  public AccountDaoImpl(DataSource dataSource) {
    this.dataSource = dataSource;
  }

//  @Override
  public Optional<AccountCreateRequest> createAccount(Connection connection, AccountCreateRequest accountCreateRequest) {
    try (PreparedStatement preparedStatement = connection.prepareStatement(CREATE_ACCOUNT)) {
      preparedStatement.setString(1, accountCreateRequest.getNumber());
      preparedStatement.setString(2, accountCreateRequest.getPhone());
      preparedStatement.executeUpdate();
      return Optional.of(accountCreateRequest);
    } catch (SQLException e) {
      logger.error(e.getMessage());
      throw new DatabaseQueryException();
    }
  }
//
//  private static Account extractCardFromResultSet(ResultSet resultSet) throws SQLException {
//    String number = resultSet.getString("number");
//    String pin = resultSet.getString("pin");
//    int accountId = resultSet.getInt("accountId");
//    Currency currency = Currency.valueOf(resultSet.getString("currency"));
//    BigDecimal balance = resultSet.getBigDecimal("balance");
//    CardStatus status = CardStatus.getCardStatusById(resultSet.getInt("status"));
//    return new Account(number, pin, accountId, currency, balance, status);
//  }
//
//  @Override
//  public Optional<Account> getCard(String numberId) throws NoSuchEntityInDatabaseException {
//    try (Connection connection = dataSource.getConnection()) {
//      try (Statement statement = connection.createStatement()) {
//        try (ResultSet resultSet = statement.executeQuery(String.format(FIND_CARD, numberId))) {
//          if (!resultSet.next()) {
//            throw new NoSuchEntityInDatabaseException(NO_SUCH_CARD);
//          }
//
//          return Optional.of(extractCardFromResultSet(resultSet));
//        }
//      }
//    } catch (SQLException e) {
//      logger.error(e.getMessage());
//      throw new DatabaseQueryException();
//    }
//  }
//
//  @Override
//  public BigDecimal getCardBalance(String numberId) throws NoSuchEntityInDatabaseException {
//    try (Connection connection = dataSource.getConnection()) {
//      try (Statement statement = connection.createStatement()) {
//        try (ResultSet resultSet = statement
//            .executeQuery(String.format(GET_CARD_BALANCE, numberId))) {
//          if (!resultSet.next()) {
//            throw new NoSuchEntityInDatabaseException(NO_SUCH_CARD);
//          }
//
//          return resultSet.getBigDecimal(1);
//        }
//      }
//    } catch (SQLException e) {
//      logger.error(e.getMessage());
//      throw new DatabaseQueryException();
//    }
//  }
//
//  @Override
//  public CardStatusUpdateRequest getCardStatus(String numberId)
//      throws NoSuchEntityInDatabaseException {
//    try (Connection connection = dataSource.getConnection()) {
//      try (Statement statement = connection.createStatement()) {
//        try (ResultSet resultSet = statement
//            .executeQuery(String.format(GET_CARD_STATUS, numberId))) {
//          if (!resultSet.next()) {
//            throw new NoSuchEntityInDatabaseException(NO_SUCH_CARD);
//          }
//          return new CardStatusUpdateRequest(numberId, resultSet.getInt(1));
//        }
//      }
//    } catch (SQLException e) {
//      logger.error(e.getMessage());
//      throw new DatabaseQueryException();
//    }
//  }
//
//  @Override
//  public List<Account> getAllCards() throws NoSuchEntityInDatabaseException {
//    try (Connection connection = dataSource.getConnection()) {
//      try (Statement statement = connection.createStatement()) {
//        try (ResultSet resultSet = statement.executeQuery(GET_ALL_FROM_CARD)) {
//          List<Account> cards = new ArrayList<>();
//          while (resultSet.next()) {
//            cards.add(extractCardFromResultSet(resultSet));
//          }
//          if (cards.isEmpty()) {
//            throw new NoSuchEntityInDatabaseException(NO_SUCH_CARD);
//          }
//
//          return cards;
//        }
//      }
//    } catch (SQLException e) {
//      logger.error(e.getMessage());
//      throw new DatabaseQueryException();
//    }
//  }
//
//  @Override
//  public List<CardStatusDescriptor> getAllStatus() throws NoSuchEntityInDatabaseException {
//    try (Connection connection = dataSource.getConnection()) {
//      try (Statement statement = connection.createStatement()) {
//        try (ResultSet resultSet = statement.executeQuery(GET_ALL_STATUS)) {
//          List<CardStatusDescriptor> statuses = new ArrayList<>();
//          while (resultSet.next()) {
//            statuses.add(new CardStatusDescriptor(resultSet.getString("number"),
//                resultSet.getString("descriptor")));
//          }
//          if (statuses.isEmpty()) {
//            throw new NoSuchEntityInDatabaseException(NO_SUCH_CARD);
//          }
//
//          return statuses;
//        }
//      }
//    } catch (SQLException e) {
//      logger.error(e.getMessage());
//      throw new DatabaseQueryException();
//    }
//  }
//
//  @Override
//  public CardStatusUpdateRequest updateCardStatus(Connection connection,
//      CardStatusUpdateRequest cardStatusUpdateRequest) {
//    try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_CARD_STATUS)) {
//      preparedStatement.setInt(1, cardStatusUpdateRequest.getStatus());
//      preparedStatement.setString(2, cardStatusUpdateRequest.getNumber());
//      if (preparedStatement.executeUpdate() == 0) {
//        throw new NoSuchEntityInDatabaseException(NO_SUCH_CARD);
//      }
//      return cardStatusUpdateRequest;
//    } catch (SQLException e) {
//      logger.error(e.getMessage());
//      throw new DatabaseQueryException();
//    }
//  }
//
//  public CardBalanceUpdateRequest updateCardBalance(Connection connection,
//      CardBalanceUpdateRequest cardBalanceUpdateRequest) throws NoSuchEntityInDatabaseException {
//    try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_CARD_BALANCE)) {
//      preparedStatement.setBigDecimal(1, cardBalanceUpdateRequest.getAmount());
//      preparedStatement.setString(2, cardBalanceUpdateRequest.getNumber());
//      if (preparedStatement.executeUpdate() == 0) {
//        throw new NoSuchEntityInDatabaseException(NO_SUCH_CARD);
//      }
//      return cardBalanceUpdateRequest;
//    } catch (SQLException e) {
//      logger.error(e.getMessage());
//      throw new DatabaseQueryException();
//    }
//  }
//
//  @Override
//  public Optional<Account> updateCard(Connection connection, Account account) {
//    try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_CARD)) {
//      preparedStatement.setBigDecimal(1, card.getBalance());
//
//      if (card.getStatus() == CardStatus.PENDING) {
//        card.setStatus(CardStatus.ACTIVE);
//      }
//      preparedStatement.setInt(2, card.getStatus().getCode());
//      preparedStatement.setString(3, card.getNumber());
//      if (preparedStatement.executeUpdate() == 0) {
//        throw new NoSuchEntityInDatabaseException(NO_SUCH_CARD);
//      }
//
//      return Optional.of(card);
//    } catch (SQLException e) {
//      logger.error(e.getMessage());
//      throw new DatabaseQueryException();
//    }
//  }
//
//
//  @Override
//  public CardNumberDeleteRequest deleteCard(Connection connection,
//      CardNumberDeleteRequest cardNumberDeleteRequest) {
//    try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_CARD)) {
//      preparedStatement.setString(1, cardNumberDeleteRequest.getNumber());
//      preparedStatement.executeUpdate();
//      return cardNumberDeleteRequest;
//    } catch (SQLException e) {
//      logger.error(e.getMessage());
//      throw new DatabaseQueryException();
//    }
//  }
}
