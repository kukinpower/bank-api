package org.romankukin.bankapi.exception;

import java.io.FileNotFoundException;

public class TransactionFailedException extends FileNotFoundException {

  private static final String message = "Transaction failed exception";

  public TransactionFailedException() {
    super(message);
  }
}
