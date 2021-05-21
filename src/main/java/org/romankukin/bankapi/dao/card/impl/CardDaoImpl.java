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
import org.romankukin.bankapi.controller.dto.CardBalanceUpdateRequest;
import org.romankukin.bankapi.controller.dto.CardStatusUpdateRequest;
import org.romankukin.bankapi.dao.Dao;
import org.romankukin.bankapi.dao.card.CardDao;
import org.romankukin.bankapi.exception.DatabaseQueryException;
import org.romankukin.bankapi.model.Card;
import org.romankukin.bankapi.model.CardStatus;
import org.romankukin.bankapi.model.Currency;

public class CardDaoImpl implements CardDao, Dao {

  private static final Logger logger = LoggerFactory.getLogger(CardDaoImpl.class);

  private final static String CREATE_TABLE = "create table IF NOT EXISTS card"
      + " (id INTEGER auto_increment primary key,"
      + " number varchar(255) NOT NULL,"
      + " pin varchar(4) NOT NULL,"
      + " balance INTEGER DEFAULT 0,"
      + "unique (number))";
  private final static String INSERT_CARD = "insert into card values(?, ?, ?, ?, ?, ?)";
  private final static String GET_ALL_FROM_CARD = "select * from card";
  private final static String FIND_CARD = "select * from card where number = '%s'";
  private final static String FIND_CARD_BY_NUMBER = "select * from card"
      + " where number = '%s'";
  private final static String GET_CARD_BALANCE = "select balance from card where number = '%s'";
  private static final String DELETE_CARD = "delete from card" +
      " where number = ? and pin = ?";

  private static final String UPDATE_CARD = "update card set balance = ?, status = ? where number = ?";
  private static final String UPDATE_CARD_STATUS = "update card set status = ? where number = ?";
  private static final String UPDATE_CARD_FIELD = "update card set %s = ? where number = ?";
  private static final String UPDATE_CARD_BALANCE = "update card set balance = balance + ? where number = ?";

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

  @Override
  public Optional<Card> getCard(String numberId) {
    try (Connection connection = dataSource.getConnection()) {
      try (Statement statement = connection.createStatement()) {
        try (ResultSet resultSet = statement.executeQuery(String.format(FIND_CARD, numberId))) {
          resultSet.next();

          return Optional.of(extractCardFromResultSet(resultSet));
        }
      }
    } catch (SQLException e) {
      logger.error(e.getMessage());
      throw new DatabaseQueryException();
    }
  }

  public BigDecimal getCardBalance(String numberId) {
    try (Connection connection = dataSource.getConnection()) {
      try (Statement statement = connection.createStatement()) {
        try (ResultSet resultSet = statement.executeQuery(String.format(GET_CARD_BALANCE, numberId))) {
          resultSet.next();

          return resultSet.getBigDecimal(1);
        }
      }
    } catch (SQLException e) {
      logger.error(e.getMessage());
      throw new DatabaseQueryException();
    }
  }

  @Override
  public List<Card> getAllEntities() {
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
    } catch (SQLException e) {
      logger.error(e.getMessage());
      throw new DatabaseQueryException();
    }
  }

  @Override
  public CardStatusUpdateRequest updateCardStatus(Connection connection,
      CardStatusUpdateRequest cardStatusUpdateRequest) {
    try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_CARD_STATUS)) {
      preparedStatement.setInt(1, cardStatusUpdateRequest.getStatus());
      preparedStatement.setString(2, cardStatusUpdateRequest.getNumber());
      preparedStatement.executeUpdate();
      return cardStatusUpdateRequest;
    } catch (SQLException e) {
      logger.error(e.getMessage());
      throw new DatabaseQueryException();
    }
  }

  public CardBalanceUpdateRequest updateCardBalance(Connection connection, CardBalanceUpdateRequest cardBalanceUpdateRequest) {
    try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_CARD_BALANCE)) {
      preparedStatement.setBigDecimal(1, cardBalanceUpdateRequest.getAmount());
      preparedStatement.setString(2, cardBalanceUpdateRequest.getNumber());
      preparedStatement.executeUpdate();

      Optional<Card> entity = getCard(cardBalanceUpdateRequest.getNumber());
      if (entity.isPresent()) {
        return entity.get();
      }
      throw new NoSuchEntityException(
          "no such entity with number: " + cardBalanceUpdateRequest.getNumber());
    } catch (SQLException e) {
      logger.error(e.getMessage());
      throw new DatabaseQueryException();
    }
  }

  @Override
  public Card updateCard(Connection connection, Card card) {
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
      logger.error(e.getMessage());
      throw new DatabaseQueryException();
    }
  }

  @Override
  public boolean createCard(Connection connection, Card card) {
    try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_CARD)) {
      preparedStatement.setString(1, card.getNumber());
      preparedStatement.setString(2, card.getPin());
      preparedStatement.setString(3, card.getAccount());
      preparedStatement.setString(4, card.getCurrency().toString());
      preparedStatement.setBigDecimal(5, card.getBalance());
      preparedStatement.setInt(6, card.getStatus().ordinal() + 1);
      preparedStatement.executeUpdate();
      return true;
    } catch (SQLException e) {
      logger.error(e.getMessage());
      throw new DatabaseQueryException();
    }
  }

  @Override
  public boolean deleteCard(Connection connection, Card card) {
    return false;
  }
}
