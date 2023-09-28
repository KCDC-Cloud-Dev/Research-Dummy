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

    public List<UserEntity> users;
    public AccountService(){
        users = new ArrayList<>();
        // Add default user
        users.add(new UserEntity("Louis"));
        users.add(new UserEntity("Clerk"));
        users.add(new UserEntity("Mario"));
    }
    public void addUser(UserDto newUser){
        var user = new UserEntity(newUser.getUserName());
        user.setAge(user.getAge());
        users.add(user);
    }
    public UserDto getUser(String name){

        UserEntity foundUser = users.stream()
                .filter(user -> user.getUserName().equals(name))
                .findFirst()
                .orElse(null);

        if (foundUser == null) {
            return null;
        }

        UserDto existUser = new UserDto();
        existUser.setUserName(foundUser.getUserName());
        existUser.setAge(foundUser.getAge());

        return existUser;
    }
    public List<UserDto> getAllUsers(){
        List<UserDto> existUsers = new ArrayList<>();
        for(var user : users ){
            var existUser = new UserDto();
            existUser.setUserName(user.getUserName());
            existUser.setAge(user.getAge());
            existUsers.add(existUser);
        }
        return existUsers;
    }
    public boolean updateUserAge(String name, int newAge){
        UserEntity foundUser = users.stream()
                .filter(user -> user.getUserName().equals(name))
                .findFirst()
                .orElse(null);
        if (foundUser == null) {
            return false;
        }
        foundUser.setAge(newAge);
        return true;
    }
    public boolean deleteUser(String username) {
        UserEntity userToRemove = users.stream()
                .filter(user -> user.getUserName().equals(username))
                .findFirst()
                .orElse(null);

        if (userToRemove == null) {
            return false;
        }

        users.remove(userToRemove);
        return true;
    }
}
