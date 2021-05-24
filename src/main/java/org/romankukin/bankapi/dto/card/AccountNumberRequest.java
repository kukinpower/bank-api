package org.romankukin.bankapi.dto.card;

public class AccountNumberRequest {

  private String account;

  public AccountNumberRequest() {
  }

  public AccountNumberRequest(String account) {
    this.account = account;
  }

  public String getAccount() {
    return account;
  }

  public void setAccount(String account) {
    this.account = account;
  }

  @Override
  public String toString() {
    return "AccountNumberRequest{" +
        "number='" + account + '\'' +
        '}';
  }
}
