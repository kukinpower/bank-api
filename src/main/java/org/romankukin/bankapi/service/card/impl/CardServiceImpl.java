package org.romankukin.bankapi.service.card.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import org.romankukin.bankapi.CardSerializer;
import org.romankukin.bankapi.controller.dto.AccountNumberRequest;
import org.romankukin.bankapi.controller.dto.CardBalanceUpdateRequest;
import org.romankukin.bankapi.controller.dto.CardStatusUpdateRequest;
import org.romankukin.bankapi.dao.TransactionalManager;
import org.romankukin.bankapi.dao.card.impl.CardDaoImpl;
import org.romankukin.bankapi.exception.NoSuchEntityInDatabaseException;
import org.romankukin.bankapi.exception.ObjectNotCreatedException;
import org.romankukin.bankapi.exception.TransactionFailedException;
import org.romankukin.bankapi.model.Card;
import org.romankukin.bankapi.model.CardStatus;
import org.romankukin.bankapi.model.Currency;
import org.romankukin.bankapi.service.card.CardService;

public class CardServiceImpl implements CardService {

  private final CardDaoImpl dao;
  private final ObjectMapper mapper;
  private final TransactionalManager transactionalManager;
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

  private Card createCardByAccountNumber(AccountNumberRequest accountNumberRequest) {
    return new Card(generateCardNumber(), generateCardPin(), accountNumberRequest.getAccount(),
        Currency.RUB, BigDecimal.ZERO, CardStatus.PENDING);
  }

  public String addNewCardToDatabase(AccountNumberRequest accountNumberRequest)
      throws JsonProcessingException {
    return addNewCardToDatabase(createCardByAccountNumber(accountNumberRequest));
  }

  public String addNewCardToDatabase(Card card) throws JsonProcessingException {
    try {
      Optional<Card> entity = transactionalManager.doTransaction((connection) -> dao.createCard(connection, card));
      if (entity.isPresent()) {
        return dtoToJson(card);
      }
    } catch (TransactionFailedException e) {
      throw new ObjectNotCreatedException(card.toString(), e);
    }
    throw new ObjectNotCreatedException(card.toString());
  }

  public String getCard(String cardNumber)
      throws JsonProcessingException, NoSuchEntityInDatabaseException {
    Optional<Card> entity = dao.getCard(cardNumber);
    if (!entity.isPresent()) {
      throw new NoSuchEntityInDatabaseException("table is empty");
    }
    return dtoToJson(entity.get());
  }

  public String updateCardStatus(CardStatusUpdateRequest cardStatusUpdate)
      throws TransactionFailedException, JsonProcessingException {
    CardStatusUpdateRequest cardStatusUpdateRequest = transactionalManager
        .doTransaction((connection) -> dao.updateCardStatus(connection, cardStatusUpdate));
    return dtoToJson(cardStatusUpdateRequest);
  }

  public String getCardBalance(String cardNumber) {
    return dao.getCardBalance(cardNumber).toPlainString();
  }

  public String getAllCards() throws JsonProcessingException, NoSuchEntityInDatabaseException {
    List<Card> cards = dao.getAllEntities();
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

  public String deposit(CardBalanceUpdateRequest cardBalanceUpdate)
      throws JsonProcessingException, TransactionFailedException {
    CardBalanceUpdateRequest cardBalanceUpdateRequest = transactionalManager
        .doTransaction((connection) -> dao.updateCardBalance(connection, cardBalanceUpdate));
    return dtoToJson(cardBalanceUpdateRequest);
  }
}
