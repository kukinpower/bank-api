package org.romankukin.bankapi.service;

import java.sql.Connection;
import org.romankukin.bankapi.dao.BankDao;
import org.romankukin.bankapi.dbconnection.DatabaseConnection;

public class AbstractDao {
  protected Connection connection;

  public AbstractDao(Connection connection) {
    this.connection = connection;
  }
}
