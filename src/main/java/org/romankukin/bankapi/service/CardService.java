package org.romankukin.bankapi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.sun.net.httpserver.HttpExchange;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import org.romankukin.bankapi.CardSerializer;
import org.romankukin.bankapi.dao.BankDao;
import org.romankukin.bankapi.exception.ObjectAlreadyExistsInDatabaseException;
import org.romankukin.bankapi.exception.ObjectNotCreatedException;
import org.romankukin.bankapi.exception.SqlExceptionToMessageConverter;
import org.romankukin.bankapi.model.Card;
import org.romankukin.bankapi.model.CardStatus;
import org.romankukin.bankapi.model.Currency;

public class CardService {

  private final BankDao<Card, String> dao;
  public static final String BIN = "400000";
  public static int CARD_LENGTH = 16;
  public static int ACCOUNT_LENGTH = 20;

  public CardService(BankDao<Card, String> dao) {
    this.dao = dao;
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
    fields.forEach((key, value) -> stringBuilder.append(key).append(":").append(value).append(System.lineSeparator()));
    return stringBuilder.toString();
  }

  public String createNewCard(HttpExchange exchange, String accountNumber)
      throws JsonProcessingException, SQLException {
    return createNewCard(exchange, new Card(generateCardNumber(), generateCardPin(), accountNumber, Currency.RUB, BigDecimal.ZERO, CardStatus.PENDING));
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

  public String getCard(HttpExchange exchange) throws SQLException, JsonProcessingException {
    Optional<Card> entity = dao.getEntity("4000002698974233");
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

  public String activateCard(HttpExchange exchange) {
    return "card: activated";
  }

  public String closeCard(HttpExchange exchange) {
    return "card: closed";
  }

  public String getAllCards() throws SQLException, JsonProcessingException {
    List<Card> cards = dao.getAllEntities();
    ObjectMapper mapper = new ObjectMapper();

    SimpleModule module = new SimpleModule();
    module.addSerializer(Card.class, new CardSerializer());
    mapper.registerModule(module);

    return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(cards);
  }
}
