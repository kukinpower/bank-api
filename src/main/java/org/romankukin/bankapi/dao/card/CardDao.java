package org.romankukin.bankapi.dao.card;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;
import org.romankukin.bankapi.controller.dto.CardStatusUpdateRequest;
import org.romankukin.bankapi.model.Card;

public interface CardDao {
  Optional<Card> getEntity(String numberId);
  List<Card> getAllEntities();
  Card updateCardStatus(Connection connection, CardStatusUpdateRequest cardStatusUpdateRequest);
  Card update(Connection connection, Card card);
  boolean create(Connection connection, Card card);
  boolean delete(Connection connection, Card card);
}
