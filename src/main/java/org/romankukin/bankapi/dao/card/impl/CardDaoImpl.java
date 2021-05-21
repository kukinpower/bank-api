package org.romankukin.bankapi.dao.card.impl;

import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import com.sun.tools.internal.ws.wsdl.framework.NoSuchEntityException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.romankukin.bankapi.controller.dto.CardStatusUpdateRequest;
import org.romankukin.bankapi.model.Card;
import org.romankukin.bankapi.model.CardStatus;
import org.romankukin.bankapi.model.Currency;

//public class CardDaoImpl implements CardDao<Card, String> {
public class CardDaoImpl {

  private static final Logger logger = LoggerFactory.getLogger(CardDaoImpl.class);

  private final static String CREATE_TABLE = "create table IF NOT EXISTS card"
      + " (id INTEGER auto_increment primary key,"
      + " number varchar(255) NOT NULL,"
      + " pin varchar(4) NOT NULL,"
      + " balance INTEGER DEFAULT 0,"
      + "unique (number))";
  private final static String INSERT_CARD = "insert into card values(?, ?, ?, ?, ?, ?)";
  private final static String GET_ALL_FROM_CARD = "select * from card";
  private final static String FIND_CARD = "select * from card"
      + " where number = '%s'";
  private final static String FIND_CARD_BY_NUMBER = "select * from card"
      + " where number = '%s'";
  private final static String GET_CARD_BALANCE = "select balance from card"
      + " where number = '%s' and pin = '%s'";
  private static final String DELETE_CARD = "delete from card" +
      " where number = ? and pin = ?";

  private static final String UPDATE_CARD = "update card set balance = ?, status = ? where number = ?";
  private static final String UPDATE_CARD_STATUS = "update card set status = ? where number = ?";
  private static final String UPDATE_CARD_FIELD = "update card set %s = ? where number = ?";
  private static final String UPDATE_CARD_BALANCE = "update card set balance = balance + ?" +
      " where number = ?";

  private DataSource dataSource;

  public CardDaoImpl(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  private static Card extractCardFromResultSet(ResultSet resultSet) throws SQLException {
    String number = resultSet.getString("number");
    String pin = resultSet.getString("pin");
    String accountId = resultSet.getString("account");
    Currency currency = Currency.valueOf(resultSet.getString("currency"));
    BigDecimal balance = resultSet.getBigDecimal("balance");
    CardStatus status = CardStatus.getCardStatusById(resultSet.getInt("status"));
    return new Card(number, pin, accountId, currency, balance, status);
  }


  public Optional<Card> getEntity(String numberId) {
    try (Connection connection = dataSource.getConnection()) {
      try (Statement statement = connection.createStatement()) {
        try (ResultSet resultSet = statement.executeQuery(String.format(FIND_CARD, numberId))) {
          resultSet.next();

          return Optional.of(extractCardFromResultSet(resultSet));
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
      return Optional.empty();
    }
  }


  public List<Card> getAllEntities() throws SQLException {
    try (Connection connection = dataSource.getConnection()) {
      try (Statement statement = connection.createStatement()) {
        try (ResultSet resultSet = statement.executeQuery(GET_ALL_FROM_CARD)) {
          List<Card> cards = new ArrayList<>();
          while (resultSet.next()) {
            cards.add(extractCardFromResultSet(resultSet));
          }
          return cards;
        }
      }
    }
  }

  public Card updateCardStatus(CardStatusUpdateRequest cardStatusUpdateRequest)
      throws SQLException {
    try (Connection connection = dataSource.getConnection()) {
      connection.setAutoCommit(false);
      try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_CARD_STATUS)) {
        preparedStatement.setInt(1, cardStatusUpdateRequest.getStatus());
        preparedStatement.setString(2, cardStatusUpdateRequest.getNumber());
        preparedStatement.executeUpdate();

        connection.commit();

        Optional<Card> entity = getEntity(cardStatusUpdateRequest.getNumber());
        if (entity.isPresent()) {
          return entity.get();
        }
        throw new NoSuchEntityException(
            "no such entity with number: " + cardStatusUpdateRequest.getNumber());
      }
    }
  }


  public Card update(Connection connection, Card card) {
    try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_CARD)) {
      preparedStatement.setBigDecimal(1, card.getBalance());

      if (card.getStatus() == CardStatus.PENDING) {
        card.setStatus(CardStatus.ACTIVE);
      }
      preparedStatement.setInt(2, card.getStatus().getCode());
      preparedStatement.setString(3, card.getNumber());
      preparedStatement.executeUpdate();

      return card;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    throw new RuntimeException();
  }


  public boolean create(Card card) throws SQLException {
    try (Connection connection = dataSource.getConnection()) {
      connection.setAutoCommit(false);
      try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_CARD)) {
        preparedStatement.setString(1, card.getNumber());
        preparedStatement.setString(2, card.getPin());
        preparedStatement.setString(3, card.getAccount());
        preparedStatement.setString(4, card.getCurrency().toString());
        preparedStatement.setBigDecimal(5, card.getBalance());
        preparedStatement.setInt(6, card.getStatus().ordinal() + 1);
        preparedStatement.executeUpdate();
        preparedStatement.close();
        connection.commit();
        return true;
      }
    }
  }


  public boolean delete(Card card) {
    return false;
  }

//  private void updateCardBalance(String number, BigDecimal amount) throws SQLException {
////    PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_CARD_BALANCE);
////    preparedStatement.setInt(1, amount.intValue()); //todo bigdecimal
////    preparedStatement.setString(2, number);
////    preparedStatement.executeUpdate();
////    preparedStatement.close();
//  }

  //  private void addIncome() throws SQLException {
////    System.out.println("Enter income:");
////    int income = scanner.nextInt();
////    updateCardBalance(currentNumber, BigDecimal.valueOf(income));
////    System.out.println("Income was added!");
//  }
//
//  private boolean findCardInDatabaseByNumber(String number) throws SQLException {
////    try (Statement statement = connection.createStatement()) {
////      ResultSet resultSet = statement.executeQuery(String.format(FIND_CARD_BY_NUMBER, number));
////
////      if (!resultSet.next()) {
////        return false;
////      }
////      return true;
////    }
//    return true;
//  }
//
  private void doTransfer() throws SQLException {
//    System.out.println("Transfer");
//    System.out.println("Enter card number:");
//    String cardNumber = scanner.next();
//    if (!cardNumber.startsWith("400000")
//        || cardNumber.length() != CardService.CARD_LENGTH
//        || !CardService.isValidCardNumberLuhnlgorithm(cardNumber)) {
//      System.out.println("Probably you made a mistake in the card number. Please try again!");
//    } else if (!findCardInDatabaseByNumber(cardNumber)) {
//      System.out.println("Such a card does not exist.");
//    } else {
//      System.out.println("Enter how much money you want to transfer:");
//      int amount = scanner.nextInt();
//      if (queryCardBalance(currentNumber, currentPin) < amount) {
//        System.out.println("Not enough money!");
//      } else {
//        connection.setAutoCommit(false);
//        updateCardBalance(cardNumber, BigDecimal.valueOf(amount));
//        updateCardBalance(currentNumber, BigDecimal.valueOf(-amount));
//        connection.commit();
//        System.out.println("Success!");
//      }
//    }
  }
//
//  private void deleteCardFromDatabase(String number, String pin) throws SQLException {
////    PreparedStatement preparedStatement = connection.prepareStatement(DELETE_CARD);
////    preparedStatement.setString(1, number);
////    preparedStatement.setString(2, pin);
////    preparedStatement.executeUpdate();
////    preparedStatement.close();
//  }
//
//  private void closeAccount() throws SQLException {
////    deleteCardFromDatabase(currentNumber, currentPin);
////    System.out.println("The account has been closed!");
//  }
//
//  private boolean findCardInDatabase(String number, String pin) throws SQLException {
////    try (Statement statement = connection.createStatement()) {
////      ResultSet resultSet = statement.executeQuery(String.format(FIND_CARD, number, pin));
////
////      if (!resultSet.next()) {
////        return false;
////      }
////      return true;
////    }
//    return true;
//  }
//
//  private int queryCardBalance(String number, String pin) throws SQLException {
////    try (Statement statement = connection.createStatement()) {
////      ResultSet resultSet = statement.executeQuery(String.format(GET_CARD_BALANCE, number, pin));
////      resultSet.next();
////      return resultSet.getInt(1);
////    }
//    return 1;
//  }
//
//  private void insertCardToTable(String number, String pin) throws SQLException {
////    PreparedStatement preparedStatement = connection.prepareStatement(INSERT_CARD);
////    preparedStatement.setString(1, number);
////    preparedStatement.setString(2, pin);
////    preparedStatement.executeUpdate();
////    preparedStatement.close();
//  }

}
