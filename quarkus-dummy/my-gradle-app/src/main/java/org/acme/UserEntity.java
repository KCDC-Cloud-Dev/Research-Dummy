package org.acme;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * @author : Mario.Yu
 * @description :
 * @date : 2023/9/27 下午 04:16
 */
@Getter
@Setter
public class UserEntity {

    private UUID userGuid = UUID.randomUUID();

    private String userName;

}
