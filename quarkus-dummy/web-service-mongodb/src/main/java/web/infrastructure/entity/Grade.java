package web.infrastructure.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;
import web.core.MongoDBDateDeserializer;

import java.util.Date;

/**
 * @author : Mario.Yu
 * @description :
 * @date : 2023/10/4 下午 03:27
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Grade  {

    private String grade;

    private int score;

    @JsonDeserialize(using = MongoDBDateDeserializer.class)
    private Date date;
}
