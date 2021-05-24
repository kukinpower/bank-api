package org.romankukin.bankapi.service.account;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.romankukin.bankapi.dto.client.ClientPhoneRequest;
import org.romankukin.bankapi.exception.NoSuchEntityInDatabaseException;
import org.romankukin.bankapi.service.BankService;

public interface AccountService extends BankService {

  String ACCOUNT_PREFIX = "2000011";

  default String generateAccountNumber() {
    return ACCOUNT_PREFIX + generateRandomIntSequenceStringOfLength(13);
  }

  String addNewAccountToDatabase(ClientPhoneRequest accountNumberRequest) throws JsonProcessingException;
  String getAllAccounts() throws JsonProcessingException, NoSuchEntityInDatabaseException;
}
