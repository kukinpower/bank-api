package org.romankukin.bankapi.service.card.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.sun.net.httpserver.HttpExchange;
import com.sun.tools.internal.ws.wsdl.framework.NoSuchEntityException;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import org.romankukin.bankapi.CardSerializer;
import org.romankukin.bankapi.controller.dto.CardBalanceUpdateRequest;
import org.romankukin.bankapi.controller.dto.CardStatusUpdateRequest;
import org.romankukin.bankapi.dao.TransactionalManager;
import org.romankukin.bankapi.dao.card.impl.CardDaoImpl;
import org.romankukin.bankapi.exception.CardClosedException;
import org.romankukin.bankapi.exception.ObjectAlreadyExistsInDatabaseException;
import org.romankukin.bankapi.exception.ObjectNotCreatedException;
import org.romankukin.bankapi.exception.SqlExceptionToMessageConverter;
import org.romankukin.bankapi.model.Card;
import org.romankukin.bankapi.model.CardStatus;
import org.romankukin.bankapi.model.Currency;
import org.romankukin.bankapi.service.card.CardService;

public class CardServiceImpl implements CardService {

  private final CardDaoImpl dao;
  private final ObjectMapper mapper;
  private TransactionalManager transactionalManager;
  public static final String BIN = "400000";
  public static int CARD_LENGTH = 16;
  public static int ACCOUNT_LENGTH = 20;

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

  public static String generateRandomIntSequenceStringOfLength(int length) {
    StringBuilder stringBuilder = new StringBuilder();

    ThreadLocalRandom random = ThreadLocalRandom.current();
    for (int i = 0; i < length; i++) {
      stringBuilder.append(random.nextInt(10));
    }
    return stringBuilder.toString();
  }

  private static int getCardSumByLuhn(String digits) {
    int[] arr = new int[digits.length()];
    for (int i = 0; i < digits.length(); i++) {
      int digit = digits.charAt(i) - '0';
      if (i % 2 == 0) {
        arr[i] = digit * 2 > 9 ? digit * 2 - 9 : digit * 2;
      } else {
        arr[i] = digit;
      }
    }

    return Arrays.stream(arr).sum();
  }

  private static boolean isValidCardNumberLuhnlgorithm(String number) {
    int sum = getCardSumByLuhn(number.substring(0, number.length() - 1));
    int checksum = Integer.parseInt(number.substring(number.length() - 1));
    return (sum + checksum) % 10 == 0;
  }

  private static String generateCardNumber() {
    String digits = BIN + generateRandomIntSequenceStringOfLength(9);
    int sum = getCardSumByLuhn(digits);
    int checksum = sum % 10 == 0 ? 0 : 10 - (sum % 10);
    return digits + checksum;
  }

  private static String generateCardPin() {
    return generateRandomIntSequenceStringOfLength(4);
  }

  private String cardFieldsToString(Map<String, Object> fields) {
    StringBuilder stringBuilder = new StringBuilder();
    fields.forEach((key, value) -> stringBuilder.append(key).append(":").append(value)
        .append(System.lineSeparator()));
    return stringBuilder.toString();
  }

  // TODO: 21.05.2021 fix
  public String createNewCard(HttpExchange exchange, JsonNode objectFromJson)
      throws IOException, SQLException {
    String accountNumber = extractFieldFromRequestBody(exchange, objectFromJson, "number");
    String currencyStringIndex = extractFieldFromRequestBody(exchange, objectFromJson, "currency");
    Currency currency = Currency.RUB;
    try {
      currency = Currency.values()[Integer.parseInt(currencyStringIndex) - 1];
    } catch (IndexOutOfBoundsException | NullPointerException e) {
      e.printStackTrace();
    }
    return createNewCard(new Card(generateCardNumber(), generateCardPin(), accountNumber, currency,
            BigDecimal.ZERO, CardStatus.PENDING));
  }

  public String createNewCard(CardBalanceUpdateRequest cardBalanceRequest) throws JsonProcessingException {

    try {
      transactionalManager.doTransaction((connection) -> dao.createCard(connection, card))
      if () {
        return cardToJson(card);
      }
    } catch (SQLException e) {
      Optional<Card> entity = dao.getCard(card.getNumber());
      if (entity.isPresent()) {
        throw new ObjectAlreadyExistsInDatabaseException(card.toString(),
            new SqlExceptionToMessageConverter().extractStackTraceFromSqlException(e),
            e);
      }
      throw new ObjectNotCreatedException(card.toString(),
          new SqlExceptionToMessageConverter().extractStackTraceFromSqlException(e),
          e);
    }
    throw new ObjectNotCreatedException(card.toString());
  }

  public String getCard(String cardNumber) throws JsonProcessingException {
    Optional<Card> entity = dao.getCard(cardNumber);
    if (!entity.isPresent()) {
      throw new NoSuchEntityException("table is empty");
    }
    return dtoToJson(entity.get());
  }

  public String updateCardStatus(CardStatusUpdateRequest cardStatusUpdate)
      throws SQLException, JsonProcessingException {
    Optional<Card> entity = dao.getCard(cardStatusUpdate.getNumber());
    if (entity.isPresent()) {
      CardStatusUpdateRequest dto = transactionalManager.doTransaction((connection) -> dao.updateCardStatus(connection, cardStatusUpdate));

      return dtoToJson(dto);

    } else {
      throw new NoSuchEntityException("no card with number: " + cardStatusUpdate.getNumber());
    }
  }

  public String getCardBalance(String cardNumber) {
    return dao.getCardBalance(cardNumber).toPlainString();
  }

  public String getAllCards() throws JsonProcessingException {
    List<Card> cards = dao.getAllEntities();
    if (cards.isEmpty()) {
      throw new NoSuchEntityException("table is empty");
    }
    return dtoToJson(cards);
  }

  private <T> String dtoToJson(T dto) throws JsonProcessingException {
    return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(dto);
  }

  private <T> String dtoToJson(List<T> dto) throws JsonProcessingException {
    return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(dto);
  }

  public String deposit(CardBalanceUpdateRequest cardBalanceUpdate) throws SQLException, JsonProcessingException {
    Optional<Card> entity = dao.getCard(cardBalanceUpdate.getNumber());
    if (entity.isPresent()) {
      Card card = entity.get();
      if (card.getStatus() == CardStatus.CLOSED) {
        throw new CardClosedException("Card " + cardBalanceUpdate.getNumber() + " is closed");
      }
      card.setBalance(card.getBalance().add(cardBalanceUpdate.getAmount()));
      Card cardFromDatabase = transactionalManager.doTransaction((connection) -> dao.updateCard(connection, card));
      return dtoToJson(cardFromDatabase);
    } else {
      throw new NoSuchEntityException("no card with number: " + cardBalanceUpdate.getNumber());
    }
  }
}
