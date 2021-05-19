package org.romankukin.bankapi.context;

import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.romankukin.bankapi.dao.CardDao;
import org.romankukin.bankapi.dbconnection.FileDatabaseConnection;
import org.romankukin.bankapi.service.CardService;

public class AppContext {

  public static final String DATA_SOURCE = "dataSource";
  public static final String CARD_DAO = "cardDao";
  public static final String CARD_SERVICE = "cardService";

  private static final Map<String, Object> beans = new HashMap<>();

  public AppContext() {
    beans.put(DATA_SOURCE, new FileDatabaseConnection().createDataSource());
    beans.put(CARD_DAO, new CardDao((DataSource) beans.get(DATA_SOURCE)));
    beans.put(CARD_SERVICE, new CardService((CardDao) beans.get(CARD_DAO)));
  }

  public Object getBean(String beanName) {
    return beans.get(beanName);
  }
}