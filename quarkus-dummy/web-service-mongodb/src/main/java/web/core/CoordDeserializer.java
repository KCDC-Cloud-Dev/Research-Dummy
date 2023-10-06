package web.core;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import web.infrastructure.entity.Coord;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : Mario.Yu
 * @description :
 * @date : 2023/10/6 下午 02:28
 */
public class CoordDeserializer extends JsonDeserializer<Coord> {
    @Override
    public Coord deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);
        Coord coord = new Coord();

        if (node.isArray()) {
            List<Double> coordinates = new ArrayList<>();
            for (JsonNode n : node) {
                coordinates.add(n.asDouble());
            }
            coord.setCoordinates(coordinates);
        }
        return coord;
    }
}
