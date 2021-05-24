package org.romankukin.bankapi.dbconnection;

import javax.sql.DataSource;
import org.h2.jdbcx.JdbcDataSource;

import java.util.Properties;

public abstract class DatabaseConnection {

  public final static String MOCK_DB_PATH = "script/create_table_mock.sql";
  public final static String EMPTY_DB_PATH = "script/create_table_empty.sql";

  protected abstract Properties extractProperties();

  public DataSource createDataSource() {
    Properties properties = extractProperties();
    JdbcDataSource jdbcDataSource = new JdbcDataSource();
    jdbcDataSource.setURL(properties.getProperty("url"));
    jdbcDataSource.setUser(properties.getProperty("user"));
    jdbcDataSource.setPassword(properties.getProperty("password"));
    return jdbcDataSource;
  }
}
