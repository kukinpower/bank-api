package org.romankukin.bankapi.dao.card;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;
import java.util.Optional;
import org.romankukin.bankapi.dto.card.CardBalanceUpdateRequest;
import org.romankukin.bankapi.dto.card.CardCreateRequest;
import org.romankukin.bankapi.dto.card.CardNumberDeleteRequest;
import org.romankukin.bankapi.dto.card.CardStatusUpdateRequest;
import org.romankukin.bankapi.dto.card.CardStatusDescriptor;
import org.romankukin.bankapi.exception.NoSuchEntityInDatabaseException;
import org.romankukin.bankapi.model.Card;

public interface CardDao {

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
