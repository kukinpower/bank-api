package org.romankukin.bankapi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
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
import org.romankukin.bankapi.dao.CardDao;
import org.romankukin.bankapi.exception.CardClosedException;
import org.romankukin.bankapi.exception.ObjectAlreadyExistsInDatabaseException;
import org.romankukin.bankapi.exception.ObjectNotCreatedException;
import org.romankukin.bankapi.exception.SqlExceptionToMessageConverter;
import org.romankukin.bankapi.model.Card;
import org.romankukin.bankapi.model.CardStatus;
import org.romankukin.bankapi.model.Currency;

public class CardService implements BankService {

  private final CardDao dao;
  private final ObjectMapper mapper;
  public static final String BIN = "400000";
  public static int CARD_LENGTH = 16;
  public static int ACCOUNT_LENGTH = 20;

  public CardService(CardDao dao) {
    this.dao = dao;
    this.mapper = createCardObjectMapper();
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

  public String createNewCard(HttpExchange exchange, JsonNode objectFromJson)
      throws IOException, SQLException {
    String accountNumber = extractFieldFromRequestBody(exchange, objectFromJson,"number");
    String currencyStringIndex = extractFieldFromRequestBody(exchange, objectFromJson, "currency");
    Currency currency = Currency.RUB;
    try {
      currency = Currency.values()[Integer.parseInt(currencyStringIndex) - 1];
    } catch (IndexOutOfBoundsException | NullPointerException e) {
      e.printStackTrace();
    }
    return createNewCard(exchange,
        new Card(generateCardNumber(), generateCardPin(), accountNumber, currency,
            BigDecimal.ZERO, CardStatus.PENDING));
  }

  public String createNewCard(HttpExchange exchange, Card card)
      throws JsonProcessingException, SQLException {

    try {
      if (dao.create(card)) {
        ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
        return objectWriter.writeValueAsString(card);
      }
    } catch (SQLException e) {
      Optional<Card> entity = dao.getEntity(card.getNumber());
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

  public String listAllCards(HttpExchange exchange) {
    return "card1, card2";
  }

  public String getCard(HttpExchange exchange, String cardNumber)
      throws SQLException, JsonProcessingException {
    Optional<Card> entity = dao.getEntity(cardNumber);
    if (entity.isPresent()) {
      ObjectMapper mapper = new ObjectMapper();

      SimpleModule module = new SimpleModule();
      module.addSerializer(Card.class, new CardSerializer());
      mapper.registerModule(module);

      return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(entity.get());
    } else {
      return "kek";
    }
  }

  public String updateCardStatus(HttpExchange exchange, CardStatusUpdateRequest cardStatusUpdate)
      throws SQLException, JsonProcessingException {
    Optional<Card> entity = dao.getEntity(cardStatusUpdate.getNumber());
    if (entity.isPresent()) {
      Card card = dao.updateCardStatus(cardStatusUpdate);

      return cardToJson(card);

    } else {
      throw new NoSuchEntityException("no card with number: " + cardStatusUpdate.getNumber());
    }
  }

  public String getCardBalance(HttpExchange exchange, String cardNumber) throws SQLException {
    Optional<Card> entity = dao.getEntity(cardNumber);
    if (entity.isPresent()) {
      return entity.get().getBalance().toPlainString();
    } else {
      throw new NoSuchEntityException("no card with number: " + cardNumber);
    }
  }

  public String getAllCards() throws SQLException, JsonProcessingException {
    return cardToJson(dao.getAllEntities());
  }

  private String cardToJson(Card card) throws JsonProcessingException {
    return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(card);
  }

  private String cardToJson(List<Card> card) throws JsonProcessingException {
    return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(card);
  }

  public String deposit(HttpExchange exchange, CardBalanceUpdateRequest cardBalanceUpdate)
      throws SQLException, JsonProcessingException {
    Optional<Card> entity = dao.getEntity(cardBalanceUpdate.getNumber());
    if (entity.isPresent()) {
      Card card = entity.get();
      if (card.getStatus() == CardStatus.CLOSED) {
        throw new CardClosedException("Card " + cardBalanceUpdate.getNumber() + " is closed");
      }
      card.setBalance(card.getBalance().add(cardBalanceUpdate.getAmount()));
      return cardToJson(dao.update(card));
    } else {
      throw new NoSuchEntityException("no card with number: " + cardBalanceUpdate.getNumber());
    }
  }
}
