package org.romankukin.bankapi.model;

import java.math.BigDecimal;

public class Card {

  private final String number;
  private final String pin;
  private final String account;
  private final Currency currency;
  private BigDecimal balance;
  private CardStatus status;

  public Card(
      String number,
      String pin,
      String account,
      Currency currency,
      BigDecimal balance,
      CardStatus status) {
    this.currency = currency;
    this.number = number;
    this.account = account;
    this.pin = pin;
    this.balance = balance;
    this.status = status;
  }

  public String getAccount() {
    return account;
  }

  public Currency getCurrency() {
    return currency;
  }

  public CardStatus getStatus() {
    return status;
  }

  public BigDecimal getBalance() {
    return balance;
  }

  public String getPin() {
    return pin;
  }

  public String getNumber() {
    return number;
  }

  public void setBalance(BigDecimal balance) {
    this.balance = balance;
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

    if (!number.equals(card.number)) {
      return false;
    }
    if (!pin.equals(card.pin)) {
      return false;
    }
    if (!account.equals(card.account)) {
      return false;
    }
    if (currency != card.currency) {
      return false;
    }
    if (!balance.equals(card.balance)) {
      return false;
    }
    return status == card.status;
  }

  @Override
  public int hashCode() {
    int result = number.hashCode();
    result = 31 * result + pin.hashCode();
    result = 31 * result + account.hashCode();
    result = 31 * result + currency.hashCode();
    result = 31 * result + balance.hashCode();
    result = 31 * result + status.hashCode();
    return result;
  }

  @Override
  public String toString() {
    return "Card{" +
        "number='" + number + '\'' +
        ", pin='" + pin + '\'' +
        ", account='" + account + '\'' +
        ", currency=" + currency +
        ", balance=" + balance +
        ", status=" + status +
        '}';
  }
}
