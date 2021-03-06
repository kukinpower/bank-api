package org.romankukin.bankapi.dbconnection;

import java.util.Properties;
import javax.sql.DataSource;
import org.h2.jdbcx.JdbcDataSource;

public abstract class DatabaseConnection {

  public final static String MOCK_DB_PATH = "script/mock_database.sql";
  public final static String CREATE_DB_PATH = "script/create_database.sql";

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
