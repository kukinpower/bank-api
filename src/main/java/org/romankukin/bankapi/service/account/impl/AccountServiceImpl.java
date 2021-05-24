package org.romankukin.bankapi.service.account.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.romankukin.bankapi.dao.account.AccountDao;
import org.romankukin.bankapi.dto.account.AccountCreateRequest;
import org.romankukin.bankapi.dto.client.ClientPhoneRequest;
import org.romankukin.bankapi.exception.NoSuchEntityInDatabaseException;
import org.romankukin.bankapi.exception.ObjectNotCreatedException;
import org.romankukin.bankapi.exception.TransactionFailedException;
import org.romankukin.bankapi.model.Account;
import org.romankukin.bankapi.model.Card;
import org.romankukin.bankapi.service.Service;
import org.romankukin.bankapi.service.account.AccountService;
import org.romankukin.bankapi.dao.TransactionalManager;

public class AccountServiceImpl implements Service, AccountService {

  private AccountDao dao;
  private final TransactionalManager transactionalManager;

  public AccountServiceImpl(AccountDao dao,
      TransactionalManager transactionalManager) {
    this.dao = dao;
    this.transactionalManager = transactionalManager;
  }

  @Override
  public String addNewAccountToDatabase(ClientPhoneRequest accountNumberRequest) throws JsonProcessingException {
    AccountCreateRequest accountCreateRequest = new AccountCreateRequest(generateAccountNumber(),
        BigDecimal.ZERO, accountNumberRequest.getPhone());
    try {
      Optional<AccountCreateRequest> entity = transactionalManager.doTransaction((connection) -> dao.createAccount(connection, accountCreateRequest));
      if (entity.isPresent()) {
        return dtoToJson(accountCreateRequest);
      }
    } catch (TransactionFailedException e) {
      throw new ObjectNotCreatedException(accountCreateRequest.toString(), e);
    }
    throw new ObjectNotCreatedException(accountCreateRequest.toString());
  }

  @Override
  public String getAllAccounts() throws JsonProcessingException, NoSuchEntityInDatabaseException {
    List<Account> cards = dao.getAllAccounts();
    if (cards.isEmpty()) {
      throw new NoSuchEntityInDatabaseException("table is empty");
    }
    return dtoToJson(cards);
  }
}
