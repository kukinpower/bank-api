package org.romankukin.bankapi.dto;

import java.math.BigDecimal;
import java.util.Objects;
import org.romankukin.bankapi.model.CardStatus;
import org.romankukin.bankapi.model.Currency;

public class CardCreateRequest {

  private String number;
  private String pin;
  private String accountNumber;
  private Currency currency;
  private BigDecimal balance;
  private CardStatus status;

  public CardCreateRequest(String number, String pin, String accountNumber,
      Currency currency, BigDecimal balance, CardStatus status) {
    this.number = number;
    this.pin = pin;
    this.accountNumber = accountNumber;
    this.currency = currency;
    this.balance = balance;
    this.status = status;
  }

  public CardCreateRequest() {
  }

  public String getNumber() {
    return number;
  }

  public String getPin() {
    return pin;
  }

  public String getAccountNumber() {
    return accountNumber;
  }

  public Currency getCurrency() {
    return currency;
  }

  public BigDecimal getBalance() {
    return balance;
  }

  public void setBalance(BigDecimal balance) {
    this.balance = balance;
  }

  public CardStatus getStatus() {
    return status;
  }

  public void setStatus(CardStatus status) {
    this.status = status;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CardCreateRequest that = (CardCreateRequest) o;
    return number.equals(that.number) && pin.equals(that.pin) && accountNumber
        .equals(that.accountNumber) && currency == that.currency && balance.equals(that.balance)
        && status == that.status;
  }

  @Override
  public int hashCode() {
    return Objects.hash(number, pin, accountNumber, currency, balance, status);
  }

  @Override
  public String toString() {
    return "CardCreateRequest{" +
        "number='" + number + '\'' +
        ", pin='" + pin + '\'' +
        ", accountNumber='" + accountNumber + '\'' +
        ", currency=" + currency +
        ", balance=" + balance +
        ", status=" + status +
        '}';
  }
}
