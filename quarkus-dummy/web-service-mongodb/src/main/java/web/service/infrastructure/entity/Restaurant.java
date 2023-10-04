package web.service.infrastructure.entity;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

/**
 * @author : Mario.Yu
 * @description :
 * @date : 2023/10/4 下午 03:36
 */
@Getter
@Setter
public class Restaurant {

    private Address address;

    private String borough;

    private String cuisine;

    private List<Grade> grades;

    private String name;


}
