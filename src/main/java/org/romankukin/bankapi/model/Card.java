package org.romankukin.bankapi.model;

import java.math.BigDecimal;
import java.util.Objects;
//import org.romankukin.bankapi.BankAccount;

public class Card {

  //  private final int cardId;//todo
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

  public void increaseBalance(BigDecimal amount) {
    balance = balance.add(amount);
  }

  public void decreaseBalance(BigDecimal amount) {
    balance = balance.subtract(amount);
  }

  public String getPin() {
    return pin;
  }

  public String getNumber() {
    return number;
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
    return number.equals(card.number) && pin.equals(card.pin) && account.equals(card.account)
        && currency == card.currency && balance.equals(card.balance) && status == card.status;
  }

  @Override
  public int hashCode() {
    return Objects.hash(number, pin, account, currency, balance, status);
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
