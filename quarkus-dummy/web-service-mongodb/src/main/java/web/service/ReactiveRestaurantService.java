package web.service;

import io.quarkus.mongodb.reactive.ReactiveMongoClient;
import io.quarkus.mongodb.reactive.ReactiveMongoCollection;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bson.Document;

import java.util.List;

/**
 * @author : Mario.Yu
 * @description :
 * @date : 2023/10/4 下午 05:23
 */
@ApplicationScoped
public class ReactiveRestaurantService {

    @Inject
    ReactiveMongoClient mongoClient;


    private ReactiveMongoCollection<Document> getCollection() {
        return mongoClient.getDatabase("sample_restaurants")
                          .getCollection("restaurants");
    }
}
