package web.core;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.util.Date;

/**
 * @author : Mario.Yu
 * @description :
 * @date : 2023/10/6 上午 11:57
 */
public class MongoDBDateDeserializer extends JsonDeserializer<Date> {

    @Override
    public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);
        JsonNode dateNode = node.get("$date");

        if (dateNode != null && !dateNode.isNull()) {
            String dateValue = dateNode.asText();
            try {
                OffsetDateTime offsetDateTime = OffsetDateTime.parse(dateValue);
                return Date.from(offsetDateTime.toInstant());
            } catch (Exception e) {
                throw new IOException("Unable to deserialize date", e);
            }
        } else {
            return null; // 或者拋出異常，根據你的需求
        }
    }
}
