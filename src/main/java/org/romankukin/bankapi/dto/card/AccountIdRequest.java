package org.romankukin.bankapi.dto.card;

public class AccountIdRequest {

  private int accountId;

  public AccountIdRequest() {
  }

  public AccountIdRequest(int accountId) {
    this.accountId = accountId;
  }

  public int getAccountId() {
    return accountId;
  }

  public void setAccountId(int accountId) {
    this.accountId = accountId;
  }

  @Override
  public String toString() {
    return "AccountNumberRequest{" +
        "accountId=" + accountId +
        '}';
  }
}
