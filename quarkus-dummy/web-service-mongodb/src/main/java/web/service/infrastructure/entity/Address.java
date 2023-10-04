package web.service.infrastructure.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author : Mario.Yu
 * @description :
 * @date : 2023/10/4 下午 03:04
 */
@Getter
@Setter
public class Address {

    private String building;

    /**
     * 座標
     */
    private Coord coord;

    /**
     * 街道
     */
    private String street;

    /**
     * Zip Code
     */
    private String zipcode;

}
