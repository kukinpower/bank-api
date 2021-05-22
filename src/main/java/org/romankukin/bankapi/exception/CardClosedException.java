package org.romankukin.bankapi.exception;

public class CardClosedException extends IllegalStateException {

  private static final String CARD_IS_CLOSED = "This card is closed";

  public CardClosedException() {
    super(CARD_IS_CLOSED);
  }
}
