package web.controller;

import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import web.infrastructure.entity.Restaurant;
import web.service.ReactiveRestaurantService;
import web.service.RestaurantService;

import java.util.List;

/**
 * @author : Mario.Yu
 * @description :
 * @date : 2023/10/6 下午 03:40
 */
@Produces(MediaType.APPLICATION_JSON)
@Path("/Restaurant")
public class RestaurantController {

    @Inject
    RestaurantService restaurantService;

    @GET
    @Path("List")
    public List<Restaurant> list() {
        return restaurantService.list();
    }
}