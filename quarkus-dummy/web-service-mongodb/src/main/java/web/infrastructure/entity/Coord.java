package web.infrastructure.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;
import web.core.CoordDeserializer;

import java.util.List;

/**
 * @author : Mario.Yu
 * @description : 座標
 * @date : 2023/10/4 下午 03:03
 */
@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(using = CoordDeserializer.class)
public class Coord {

    private List<Double> coordinates;
}
