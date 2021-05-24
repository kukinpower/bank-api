package org.romankukin.bankapi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public interface BankService {

  ObjectMapper mapper = new ObjectMapper();
  String TABLE_IS_EMPTY = "Table is empty";

  default String generateRandomIntSequenceStringOfLength(int length) {
    StringBuilder stringBuilder = new StringBuilder();

    ThreadLocalRandom random = ThreadLocalRandom.current();
    for (int i = 0; i < length; i++) {
      stringBuilder.append(random.nextInt(10));
    }
    return stringBuilder.toString();
  }

  default <T> String dtoToJson(T dto) throws JsonProcessingException {
    return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(dto);
  }

  default <T> String dtoToJson(List<T> dto) throws JsonProcessingException {
    return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(dto);
  }
}
