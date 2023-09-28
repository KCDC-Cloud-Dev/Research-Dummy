package org.acme;

import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : Mario.Yu
 * @description :
 * @date : 2023/9/27 下午 04:15
 */
@Produces(MediaType.APPLICATION_JSON)
@Path("/Account")
public class AccountController {

    @Inject
    AccountService accountService;

    @POST
    public Uni<Response> createAccount(UserDto user){
        return Uni.createFrom().item(() -> {
            accountService.addUser(user);
            return Response.status(Response.Status.CREATED).build();
        });

    }

    @GET
    @Path("{name}")
    public Uni<Response> getAccount(String name){
        return Uni.createFrom().item(() -> {
            UserDto existUser = accountService.getUser(name);  // 注意這裡，我使用了你先前定義的 `getUser` 方法
            if (existUser == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            return Response.ok(existUser).build();
        });
    }

    /*
  高併發寫法
   */
    @GET
    @Path("/GetAccounts")
    public Uni<Response> getAccounts() {
        return Uni.createFrom().item(() -> {
            return Response.ok(accountService.getAllUsers()).build(); // 注意這裡，直接將 List<UserDto> 放入 Response
        });
    }

    /*
    非高併發寫法((注意))
     */
    @GET
    @Path("/NotConcurrencyGetAccounts")
    public List<UserDto> notConcurrencyGetAccounts() {
        var accounts = accountService.getAllUsers();
        List<UserDto> users = new ArrayList<>();
        for(var account : accounts) {
            UserDto user = new UserDto();
            user.setUserName(account.getUserName());
            user.setAge(account.getAge());
            users.add(user);
        }
        return users;
    }

    @PUT
    @Path("{username}")
    public Uni<Response> updateAge(@PathParam("username") String username, @QueryParam("age") int age){
        return Uni.createFrom().item(() -> {
            boolean updated = accountService.updateUserAge(username, age);
            if (updated) {
                return Response.status(Response.Status.OK).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        });
    }


    @DELETE
    @Path("{username}")
    public Uni<Response> deleteAccount(@PathParam("username") String username){
        return Uni.createFrom().item(() -> {
            boolean deleted = accountService.deleteUser(username);
            if (deleted) {
                return Response.status(Response.Status.OK).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        });
    }

}
