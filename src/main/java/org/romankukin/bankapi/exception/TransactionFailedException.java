package org.romankukin.bankapi.exception;

import java.sql.SQLException;

public class TransactionFailedException extends SQLException {

  private static final String message = "Transaction failed exception";

  public TransactionFailedException() {
    super(message);
  }
}
