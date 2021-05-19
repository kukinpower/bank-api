package org.romankukin.bankapi.model;

public enum CardStatus {
  PENDING(1), ACTIVE(2), CLOSED(3);

  private final int code;

  CardStatus(int code) {
    this.code = code;
  }

  public int getCode() {
    return code;
  }
}
