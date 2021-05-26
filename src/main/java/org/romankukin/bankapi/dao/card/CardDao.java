package org.romankukin.bankapi.dao.card;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import org.romankukin.bankapi.dto.card.CardBalanceUpdateRequest;
import org.romankukin.bankapi.dto.card.CardCreateRequest;
import org.romankukin.bankapi.dto.card.CardNumberDeleteRequest;
import org.romankukin.bankapi.dto.card.CardStatusDescriptor;
import org.romankukin.bankapi.dto.card.CardStatusUpdateRequest;
import org.romankukin.bankapi.exception.NoSuchEntityInDatabaseException;
import org.romankukin.bankapi.model.Card;
import org.romankukin.bankapi.model.CardStatus;
import org.romankukin.bankapi.model.Currency;

public interface CardDao {

  default Card extractCardFromResultSet(ResultSet resultSet) throws SQLException {
    String number = resultSet.getString("number");
    String pin = resultSet.getString("pin");
    int accountId = resultSet.getInt("accountId");
    Currency currency = Currency.valueOf(resultSet.getString("currency"));
    BigDecimal balance = resultSet.getBigDecimal("balance");
    CardStatus status = CardStatus.getCardStatusById(resultSet.getInt("status"));
    return new Card(number, pin, accountId, currency, balance, status);
  }

  Optional<Card> getCard(String numberId) throws NoSuchEntityInDatabaseException;

  List<Card> getAllCards() throws NoSuchEntityInDatabaseException;

  List<CardStatusDescriptor> getAllStatus() throws NoSuchEntityInDatabaseException;

  BigDecimal getCardBalance(String numberId) throws NoSuchEntityInDatabaseException;

  CardStatusUpdateRequest getCardStatus(String numberId) throws NoSuchEntityInDatabaseException;

  CardStatusUpdateRequest updateCardStatus(Connection connection, CardStatusUpdateRequest cardStatusUpdateRequest);

  CardBalanceUpdateRequest updateCardBalance(Connection connection, CardBalanceUpdateRequest cardBalanceUpdateRequest)
      throws NoSuchEntityInDatabaseException;

  Optional<Card> updateCard(Connection connection, Card card);

  Optional<CardCreateRequest> createCard(Connection connection, CardCreateRequest card);

  CardNumberDeleteRequest deleteCard(Connection connection, CardNumberDeleteRequest cardNumberDeleteRequest);
}
