package org.romankukin.bankapi.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import javax.sql.DataSource;
import org.romankukin.bankapi.model.Card;
import org.romankukin.bankapi.model.CardStatus;
import org.romankukin.bankapi.model.Currency;

public class CardDao implements BankDao<Card, String> {

  private static final Logger logger = Logger.getLogger(CardDao.class.getName());

  private final static String CREATE_TABLE = "create table IF NOT EXISTS card"
      + " (id INTEGER auto_increment primary key,"
      + " number varchar(255) NOT NULL,"
      + " pin varchar(4) NOT NULL,"
      + " balance INTEGER DEFAULT 0,"
      + "unique (number))";
  private final static String INSERT_CARD = "insert into card values(?, ?, ?, ?, ?, ?)";
  private final static String FIND_CARD = "select * from card"
      + " where number = '%s'";
  private final static String FIND_CARD_BY_NUMBER = "select * from card"
      + " where number = '%s'";
  private final static String GET_CARD_BALANCE = "select balance from card"
      + " where number = '%s' and pin = '%s'";
  private static final String DELETE_CARD = "delete from card" +
      " where number = ? and pin = ?";


  private static final String UPDATE_CARD_BALANCE = "update card set balance = balance + ?" +
      " where number = ?";

  private DataSource dataSource;

  public CardDao(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public Optional<Card> getEntity(String numberId) {
    try (Connection connection = dataSource.getConnection()) {
      Statement statement = connection.createStatement();
      ResultSet resultSet = statement.executeQuery(String.format(FIND_CARD, numberId));
      resultSet.next();
      String number = resultSet.getString("number");
      String pin = resultSet.getString("pin");
      Integer accountId = resultSet.getInt("account");
      Currency currency = Currency.valueOf(resultSet.getString("currency"));
      BigDecimal balance = BigDecimal.valueOf(resultSet.getDouble("balance"));
      CardStatus status = CardStatus.values()[resultSet.getInt("status") - 1];

      return Optional.of(new Card(number, pin, accountId, currency, balance, status));

    } catch (SQLException e) {
      e.printStackTrace();
      return Optional.empty();
    }
  }

  @Override
  public List<Card> getAllEntities() {
    return null;
  }

  @Override
  public Card update(Card card, String[] params) {
    return null;
  }

  @Override
  public boolean create(Card card) throws SQLException {
    try (Connection connection = dataSource.getConnection()) {
      PreparedStatement preparedStatement = connection.prepareStatement(INSERT_CARD);
      preparedStatement.setString(1, card.getNumber());
      preparedStatement.setString(2, card.getPin());
      preparedStatement.setInt(3, card.getAccountId());
      preparedStatement.setString(4, card.getCurrency().toString());
      preparedStatement.setDouble(5, card.getBalance().doubleValue());
      preparedStatement.setInt(6, card.getStatus().ordinal() + 1);
      preparedStatement.executeUpdate();
      preparedStatement.close();
      return true;
    }
  }

  @Override
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
