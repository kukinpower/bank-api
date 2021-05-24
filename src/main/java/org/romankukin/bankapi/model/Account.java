package org.romankukin.bankapi.model;

import java.util.Objects;

public class Account {

  private String number;
  private String balance;
  private int clientId;

  public Account() {
  }

  public Account(String number, String balance, int clientId) {
    this.number = number;
    this.balance = balance;
    this.clientId = clientId;
  }

  public String getNumber() {
    return number;
  }

  public void setNumber(String number) {
    this.number = number;
  }

  public String getBalance() {
    return balance;
  }

  public void setBalance(String balance) {
    this.balance = balance;
  }

  public int getClientId() {
    return clientId;
  }

  public void setClientId(int clientId) {
    this.clientId = clientId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Account account = (Account) o;
    return clientId == account.clientId && number.equals(account.number) && balance
        .equals(account.balance);
  }

  @Override
  public int hashCode() {
    return Objects.hash(number, balance, clientId);
  }

  @Override
  public String toString() {
    return "Account{" +
        "number='" + number + '\'' +
        ", balance='" + balance + '\'' +
        ", clientId=" + clientId +
        '}';
  }
}
