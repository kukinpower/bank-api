package org.romankukin.bankapi.exception;

public class NoRequestBodyDetectedException extends IllegalArgumentException {

  public NoRequestBodyDetectedException() {
  }

  public NoRequestBodyDetectedException(String s) {
    super(s);
  }

  public NoRequestBodyDetectedException(String message, Throwable cause) {
    super(message, cause);
  }

  public NoRequestBodyDetectedException(Throwable cause) {
    super(cause);
  }
}
