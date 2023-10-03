package org.acme;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author : Mario.Yu
 * @description :
 * @date : 2023/9/27 下午 04:36
 */

@Setter
@Getter
public class UserDto {

    private String userName;

    private int age;
}
