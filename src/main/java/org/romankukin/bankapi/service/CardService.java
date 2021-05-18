package org.romankukin.bankapi.service;

import com.sun.net.httpserver.HttpExchange;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import org.romankukin.bankapi.dao.BankDao;
import org.romankukin.bankapi.dao.CardDao;
import org.romankukin.bankapi.model.Card;

public class CardService {

  private final BankDao<Card, String> dao;
  public static final String BIN = "400000";
  public static int CARD_LENGTH = 16;

  public CardService(BankDao<Card, String> dao) {
    this.dao = dao;
  }

  private static String generateRandomIntSequenceStringOfLength(int length) {
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

  public String addNewCard(HttpExchange exchange) {
    return "new card added";
  }

  public String listAllCards(HttpExchange exchange) {
    return "card1, card2";
  }

  public String getCard(HttpExchange exchange) {
    Optional<Card> entity = dao.getEntity("1234567890123456");
    if (entity.isPresent()) {
      return entity.get().toString();
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

}
