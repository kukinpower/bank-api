package org.romankukin.bankapi.dao.card;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;
import org.romankukin.bankapi.controller.dto.CardBalanceUpdateRequest;
import org.romankukin.bankapi.controller.dto.CardStatusUpdateRequest;
import org.romankukin.bankapi.model.Card;

public interface CardDao {

  Optional<Card> getCard(String numberId);

  List<Card> getAllEntities();

  CardStatusUpdateRequest updateCardStatus(Connection connection, CardStatusUpdateRequest cardStatusUpdateRequest);

  CardBalanceUpdateRequest updateCardBalance(Connection connection, CardBalanceUpdateRequest cardBalanceUpdateRequest);

  Card updateCard(Connection connection, Card card);

  boolean createCard(Connection connection, Card card);

  boolean deleteCard(Connection connection, Card card);
}
