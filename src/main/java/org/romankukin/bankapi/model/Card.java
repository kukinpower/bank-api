package org.romankukin.bankapi.model;

import java.math.BigDecimal;
//import org.romankukin.bankapi.BankAccount;

public class Card {

//  private final int cardId;//todo
//  private int bankAccount;
  private final String number;
  private final String pin;
  private final Currency currency;
  private BigDecimal balance;
  private CardStatus status;

  public Card(Currency currency, String number, String pin, BigDecimal balance,
      CardStatus status) {
    this.currency = currency;
    this.number = number;
    this.pin = pin;
    this.balance = balance;
    this.status = status;
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

    if (!pin.equals(card.pin)) {
      return false;
    }
    if (!number.equals(card.number)) {
      return false;
    }
    return balance.equals(card.balance);
  }

  @Override
  public int hashCode() {
    int result = pin.hashCode();
    result = 31 * result + number.hashCode();
    return result;
  }

  @Override
  public String toString() {
    return "Card{" +
        "pin='" + pin + '\'' +
        ", number='" + number + '\'' +
        ", balance=" + balance +
        '}';
  }
}
