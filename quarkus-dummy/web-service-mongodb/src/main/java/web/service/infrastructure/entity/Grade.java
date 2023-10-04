package web.service.infrastructure.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author : Mario.Yu
 * @description :
 * @date : 2023/10/4 下午 03:27
 */
@Getter
@Setter
public class Grade {

    private String grade;

    private int score;

    private Date date;
}
