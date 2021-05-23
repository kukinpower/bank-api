package org.romankukin.bankapi.service.card.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.romankukin.bankapi.CardSerializer;
import org.romankukin.bankapi.dao.TransactionalManager;
import org.romankukin.bankapi.dao.card.impl.CardDaoImpl;
import org.romankukin.bankapi.dto.AccountNumberRequest;
import org.romankukin.bankapi.dto.CardBalanceUpdateRequest;
import org.romankukin.bankapi.dto.CardNumberDeleteRequest;
import org.romankukin.bankapi.dto.CardStatusDescriptor;
import org.romankukin.bankapi.dto.CardStatusUpdateRequest;
import org.romankukin.bankapi.exception.NoSuchEntityInDatabaseException;
import org.romankukin.bankapi.exception.ObjectNotCreatedException;
import org.romankukin.bankapi.exception.TransactionFailedException;
import org.romankukin.bankapi.model.Card;
import org.romankukin.bankapi.model.CardStatus;
import org.romankukin.bankapi.model.Currency;
import org.romankukin.bankapi.service.BankService;
import org.romankukin.bankapi.service.Service;
import org.romankukin.bankapi.service.card.CardService;

public class CardServiceImpl implements Service, CardService, BankService {

  private final CardDaoImpl dao;
  private final ObjectMapper mapper;
  private final TransactionalManager transactionalManager;

  public CardServiceImpl(CardDaoImpl dao, TransactionalManager transactionalManager) {
    this.dao = dao;
    this.mapper = createCardObjectMapper();
    this.transactionalManager = transactionalManager;
  }

  private static ObjectMapper createCardObjectMapper() {
    ObjectMapper mapper = new ObjectMapper();
    SimpleModule module = new SimpleModule();
    module.addSerializer(Card.class, new CardSerializer());
    mapper.registerModule(module);
    return mapper;
  }

  private Card createCardByAccountNumber(AccountNumberRequest accountNumberRequest) {
    return new Card(generateCardNumber(), generateCardPin(), accountNumberRequest.getAccount(),
        Currency.RUB, BigDecimal.ZERO, CardStatus.PENDING);
  }

  public String addNewCardToDatabase(AccountNumberRequest accountNumberRequest)
      throws JsonProcessingException {
    return addNewCardToDatabase(createCardByAccountNumber(accountNumberRequest));
  }

  public String addNewCardToDatabase(Card card) throws JsonProcessingException {
    validateCardNumber(card.getNumber());
    try {
      Optional<Card> entity = transactionalManager
          .doTransaction((connection) -> dao.createCard(connection, card));
      if (entity.isPresent()) {
        return dtoToJson(card);
      }
    } catch (TransactionFailedException e) {
      throw new ObjectNotCreatedException(card.toString(), e);
    }
    throw new ObjectNotCreatedException(card.toString());
  }

  private void validateCardNumber(String cardNumber) {
    if (!cardNumber.matches("^[0-9]{16}$") || isValidCardNumberLuhnAlgorithm(cardNumber)) {
      throw new IllegalArgumentException("Bad card number argument: " + cardNumber);
    }
  }

  public String getCard(String cardNumber)
      throws JsonProcessingException, NoSuchEntityInDatabaseException {
    validateCardNumber(cardNumber);
    Optional<Card> entity = dao.getCard(cardNumber);
    if (!entity.isPresent()) {
      throw new NoSuchEntityInDatabaseException("table is empty");
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
    List<Card> cards = dao.getAllEntities();
    if (cards.isEmpty()) {
      throw new NoSuchEntityInDatabaseException("table is empty");
    }
    return dtoToJson(cards);
  }

  public String getAllCardsStatus() throws JsonProcessingException, NoSuchEntityInDatabaseException {
    List<CardStatusDescriptor> cards = dao.getAllStatus();
    if (cards.isEmpty()) {
      throw new NoSuchEntityInDatabaseException("table is empty");
    }
    return dtoToJson(cards);
  }

  private <T> String dtoToJson(T dto) throws JsonProcessingException {
    return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(dto);
  }

  private <T> String dtoToJson(List<T> dto) throws JsonProcessingException {
    return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(dto);
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
