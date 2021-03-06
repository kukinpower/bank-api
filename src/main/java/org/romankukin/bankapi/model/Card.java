package org.romankukin.bankapi.model;

import java.math.BigDecimal;
import java.util.Objects;

public class Card {

  private final String number;
  private final String pin;
  private final int accountId;
  private final Currency currency;
  private BigDecimal balance;
  private CardStatus status;

  public Card(String number, String pin, int accountId,
      Currency currency, BigDecimal balance, CardStatus status) {
    this.number = number;
    this.pin = pin;
    this.accountId = accountId;
    this.currency = currency;
    this.balance = balance;
    this.status = status;
  }

  public String getNumber() {
    return number;
  }

  public String getPin() {
    return pin;
  }

  public int getAccountId() {
    return accountId;
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
    Card card = (Card) o;
    return accountId == card.accountId && number.equals(card.number) && pin.equals(card.pin)
        && currency == card.currency && balance.equals(card.balance) && status == card.status;
  }

  @Override
  public int hashCode() {
    return Objects.hash(number, pin, accountId, currency, balance, status);
  }

  @Override
  public String toString() {
    return "Card{" +
        "number='" + number + '\'' +
        ", pin='" + pin + '\'' +
        ", accountId=" + accountId +
        ", currency=" + currency +
        ", balance=" + balance +
        ", status=" + status +
        '}';
  }
}
