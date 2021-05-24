package org.romankukin.bankapi.service.card.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.romankukin.bankapi.dao.TransactionalManager;
import org.romankukin.bankapi.dao.card.impl.CardDaoImpl;
import org.romankukin.bankapi.dto.card.AccountNumberRequest;
import org.romankukin.bankapi.dto.card.CardBalanceUpdateRequest;
import org.romankukin.bankapi.dto.card.CardCreateRequest;
import org.romankukin.bankapi.dto.card.CardNumberDeleteRequest;
import org.romankukin.bankapi.dto.card.CardStatusDescriptor;
import org.romankukin.bankapi.dto.card.CardStatusUpdateRequest;
import org.romankukin.bankapi.exception.NoSuchEntityInDatabaseException;
import org.romankukin.bankapi.exception.ObjectNotCreatedException;
import org.romankukin.bankapi.exception.TransactionFailedException;
import org.romankukin.bankapi.model.Card;
import org.romankukin.bankapi.model.CardStatus;
import org.romankukin.bankapi.model.Currency;
import org.romankukin.bankapi.service.BankService;
import org.romankukin.bankapi.service.Service;
import org.romankukin.bankapi.service.card.CardService;

public class CardServiceImpl implements Service, BankService, CardService {

  private final CardDaoImpl dao;
  private final ObjectMapper mapper;
  private final TransactionalManager transactionalManager;

  public CardServiceImpl(CardDaoImpl dao, TransactionalManager transactionalManager) {
    this.dao = dao;
    this.mapper = new ObjectMapper();
    this.transactionalManager = transactionalManager;
  }

  public String addNewCardToDatabase(AccountNumberRequest accountNumberRequest) throws JsonProcessingException {
    CardCreateRequest cardCreateRequest = new CardCreateRequest(generateCardNumber(),
        generateCardPin(), accountNumberRequest.getAccount(),
        Currency.RUB, BigDecimal.ZERO, CardStatus.PENDING);

    validateCardNumber(cardCreateRequest.getNumber());
    try {
      Optional<CardCreateRequest> entity = transactionalManager
          .doTransaction((connection) -> dao.createCard(connection, cardCreateRequest));
      if (entity.isPresent()) {
        return dtoToJson(cardCreateRequest);
      }
    } catch (TransactionFailedException e) {
      throw new ObjectNotCreatedException(cardCreateRequest.toString(), e);
    }
    throw new ObjectNotCreatedException(cardCreateRequest.toString());
  }

  private void validateCardNumber(String cardNumber) {
    if (!cardNumber.matches("^[0-9]{16}$") || !isValidCardNumberLuhnAlgorithm(cardNumber)) {
      throw new IllegalArgumentException("Bad card number argument: " + cardNumber);
    }
  }

  public String getCard(String cardNumber)
      throws JsonProcessingException, NoSuchEntityInDatabaseException {
    validateCardNumber(cardNumber);
    Optional<Card> entity = dao.getCard(cardNumber);
    if (!entity.isPresent()) {
      throw new NoSuchEntityInDatabaseException(TABLE_IS_EMPTY);
    }
    return dtoToJson(entity.get());
  }

  public String updateCardStatus(CardStatusUpdateRequest cardStatusUpdate)
      throws TransactionFailedException, JsonProcessingException {
    validateCardNumber(cardStatusUpdate.getNumber());
    CardStatusUpdateRequest cardStatusUpdateRequest = transactionalManager
        .doTransaction((connection) -> dao.updateCardStatus(connection, cardStatusUpdate));
    return dtoToJson(cardStatusUpdateRequest);
  }

  public String getCardBalance(String cardNumber) throws NoSuchEntityInDatabaseException {
    validateCardNumber(cardNumber);
    return dao.getCardBalance(cardNumber).toPlainString();
  }

  public String getCardStatus(String cardNumber)
      throws NoSuchEntityInDatabaseException, JsonProcessingException {
    validateCardNumber(cardNumber);
    return dtoToJson(dao.getCardStatus(cardNumber));
  }

  public String getAllCards() throws JsonProcessingException, NoSuchEntityInDatabaseException {
    List<Card> cards = dao.getAllCards();
    if (cards.isEmpty()) {
      throw new NoSuchEntityInDatabaseException(TABLE_IS_EMPTY);
    }
    return dtoToJson(cards);
  }

  public String getAllCardsStatus() throws JsonProcessingException, NoSuchEntityInDatabaseException {
    List<CardStatusDescriptor> cards = dao.getAllStatus();
    if (cards.isEmpty()) {
      throw new NoSuchEntityInDatabaseException(TABLE_IS_EMPTY);
    }
    return dtoToJson(cards);
  }

  public String depositCard(CardBalanceUpdateRequest cardBalanceUpdate)
      throws JsonProcessingException, TransactionFailedException, NoSuchEntityInDatabaseException {
    validateCardNumber(cardBalanceUpdate.getNumber());
    CardBalanceUpdateRequest cardBalanceUpdateRequest = transactionalManager
        .doTransaction((connection) -> dao.updateCardBalance(connection, cardBalanceUpdate));
    return dtoToJson(cardBalanceUpdateRequest);
  }

  public String deleteCard(CardNumberDeleteRequest cardNumberDeleteRequest)
      throws JsonProcessingException, TransactionFailedException {
    validateCardNumber(cardNumberDeleteRequest.getNumber());
    CardNumberDeleteRequest cardNumberDelete = transactionalManager
        .doTransaction((connection) -> dao.deleteCard(connection, cardNumberDeleteRequest));
    return dtoToJson(cardNumberDelete);
  }
}
