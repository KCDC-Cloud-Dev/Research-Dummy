package org.acme;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/hello")
public class GreetingResource {

    @Inject
    AccountService accountService;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return accountService.GetAccountName();
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/greeting")
    public String fuck() {
        return "Hello Fuck";
    }


}
