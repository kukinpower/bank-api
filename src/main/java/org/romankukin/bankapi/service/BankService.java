package org.romankukin.bankapi.service;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public interface BankService {
  String BIN = "400000";

  default String generateRandomIntSequenceStringOfLength(int length) {
    StringBuilder stringBuilder = new StringBuilder();

    ThreadLocalRandom random = ThreadLocalRandom.current();
    for (int i = 0; i < length; i++) {
      stringBuilder.append(random.nextInt(10));
    }
    return stringBuilder.toString();
  }

  default int getCardSumByLuhn(String digits) {
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

  default boolean isValidCardNumberLuhnAlgorithm(String number) {
    int sum = getCardSumByLuhn(number.substring(0, number.length() - 1));
    int checksum = Integer.parseInt(number.substring(number.length() - 1));
    return (sum + checksum) % 10 == 0;
  }

  default String generateCardNumber() {
    String digits = BIN + generateRandomIntSequenceStringOfLength(9);
    int sum = getCardSumByLuhn(digits);
    int checksum = sum % 10 == 0 ? 0 : 10 - (sum % 10);
    return digits + checksum;
  }

  default String generateCardPin() {
    return generateRandomIntSequenceStringOfLength(4);
  }

}
