package web.service;

import io.quarkus.mongodb.reactive.ReactiveMongoClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * @author : Mario.Yu
 * @description :
 * @date : 2023/10/4 下午 05:23
 */
@ApplicationScoped
public class ReactiveRestaurantService {

    @Inject
    ReactiveMongoClient mongoClient;

    

}
