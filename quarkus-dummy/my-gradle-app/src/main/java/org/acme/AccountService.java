package org.acme;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : Mario.Yu
 * @description :
 * @date : 2023/9/27 下午 02:34
 */

@ApplicationScoped
public class AccountService {

    public List<UserEntity> Users;
    public AccountService(){
        Users = new ArrayList<>();
    }

    public List<UserEntity> GetAllUsers(){
        return Users;
    }
    public String GetAccountName(){
        return  "Mario";
    }
}
