package org.romankukin.bankapi.exception;

public class CardClosedException extends IllegalStateException {

  public CardClosedException() {
  }

  public CardClosedException(String s) {
    super(s);
  }

  public CardClosedException(String message, Throwable cause) {
    super(message, cause);
  }

  public CardClosedException(Throwable cause) {
    super(cause);
  }
}
