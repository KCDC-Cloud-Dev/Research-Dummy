package org.acme;

import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : Mario.Yu
 * @description :
 * @date : 2023/9/27 下午 04:15
 */
@Path("/Account")
public class AccountController {

    @Inject
    AccountService accountService;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/GetAccounts")
    public Uni<Response> GetAccounts() {

        return Uni.createFrom().item(() -> {
            List<UserDto> users = new ArrayList<>();
            UserDto user = new UserDto();
            user.setUserName("Marioo");
            users.add(user);

            return Response.ok(users).build(); // 注意這裡，直接將 List<UserDto> 放入 Response
        });
        /*
        var accounts = accountService.GetAllUsers();

        List<UserDto> users = new ArrayList<>();
        UserDto user = new UserDto();
        user.setUserName("Marioo");
        users.add(user);
        /*
        for(var account : accounts) {
            UserDto user = new UserDto();
            user.setUserName("");
            users.add(user);
        }
        */
        //return users;
    }



}
