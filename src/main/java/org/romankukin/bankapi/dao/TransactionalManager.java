package org.romankukin.bankapi.dao;

import org.romankukin.bankapi.dao.card.SupplierDao;
import org.romankukin.bankapi.exception.TransactionFailedException;

public interface TransactionalManager {

  <T> T doTransaction(SupplierDao<T> action) throws TransactionFailedException;
}
