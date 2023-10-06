package web.controller;

import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import web.infrastructure.entity.Restaurant;
import web.service.ReactiveGradeTestService;
import web.service.ReactiveRestaurantService;
import web.infrastructure.entity.Grade;

import java.util.List;

/**
 * @author : Mario.Yu
 * @description :
 * @date : 2023/10/6 上午 09:24
 */
@Produces(MediaType.APPLICATION_JSON)
@Path("/Restaurant")
public class RestaurantController {


    @Inject
    ReactiveRestaurantService reactiveRestaurantService;

    @GET
    @Path("List")
    public Uni<List<Restaurant>> list() {
        return reactiveRestaurantService.list();
    }

}
