package org.romankukin.bankapi;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import org.romankukin.bankapi.model.Card;

public class CardSerializer extends StdSerializer<Card> {

  public CardSerializer(Class<Card> t) {
    super(t);
  }

  public CardSerializer() {
    this(null);
//    super();
  }

  @Override
  public void serialize(Card card, JsonGenerator jsonGenerator,
      SerializerProvider serializerProvider) throws IOException {
    jsonGenerator.writeStartObject();
    jsonGenerator.writeStringField("number", card.getNumber());
    jsonGenerator.writeStringField("pin", card.getPin());
    jsonGenerator.writeStringField("account", card.getAccount());
    jsonGenerator.writeStringField("currency", card.getCurrency().name());
    jsonGenerator.writeNumberField("balance", card.getBalance());
    jsonGenerator.writeNumberField("status", card.getStatus().getCode());
    jsonGenerator.writeEndObject();
  }
}
