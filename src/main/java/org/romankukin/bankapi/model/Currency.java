package org.romankukin.bankapi.model;

public enum Currency {
  RUB(1), USD(2), EUR(3);

  private final int code;

  Currency(int code) {
    this.code = code;
  }

  public int getCode() {
    return code;
  }

  public static Currency getCurrencyById(int id) {
    return Currency.values()[id - 1];
  }
}
