package web.service;

import io.quarkus.mongodb.reactive.ReactiveMongoClient;
import io.quarkus.mongodb.reactive.ReactiveMongoCollection;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.core.Vertx;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import mutiny.zero.flow.adapters.AdaptersToFlow;
import org.bson.Document;
import org.jboss.logging.Logger;
import web.infrastructure.entity.Address;
import web.infrastructure.entity.Coord;
import web.infrastructure.entity.Grade;
import web.infrastructure.entity.Restaurant;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoCollection;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author : Mario.Yu
 * @description :
 * @date : 2023/10/4 下午 05:23
 */
@ApplicationScoped
public class ReactiveRestaurantService {

    private final static Logger logger = Logger.getLogger(ReactiveRestaurantService.class);

    @Inject
    MongoClient mongoClient;

    @Inject
    ReactiveMongoClient reactiveMongoClient;

    public Uni<List<Restaurant>> list(){

        return getReactiveCollection().find()
                .map(document -> {
                    // 幫我補上map..用doc.getString...getInterage寫法
                    Restaurant restaurant = new Restaurant();
                    restaurant.setBorough(document.getString("borough"));
                    restaurant.setCuisine(document.getString("cuisine"));
                    restaurant.setName(document.getString("name"));

                    Document addressDoc = document.get("address", Document.class);
                    if (addressDoc != null) {
                        Address address = new Address();
                        address.setBuilding(addressDoc.getString("building"));
                        address.setStreet(addressDoc.getString("street"));
                        address.setZipcode(addressDoc.getString("zipcode"));

                        List<Double> coordList = addressDoc.get("coord", List.class);
                        if (coordList != null) {
                            Coord coord = new Coord();
                            coord.setCoordinates(coordList);
                            address.setCoord(coord);
                        }

                        restaurant.setAddress(address);
                    }

                    // 下面是處理 grades 的代碼。因為它是一個複雜的字段，所以我特別為它寫了一段處理邏輯。
                    List<Document> gradesList = document.get("grades", List.class);
                    if (gradesList != null) {
                        List<Grade> grades = gradesList.stream()
                                .map(gradeDoc -> {
                                    Grade grade = new Grade();
                                    grade.setGrade(gradeDoc.getString("grade"));

                                    grade.setScore(Objects.requireNonNullElse(gradeDoc.getInteger("score"), 0));

                                    // 處理 date 字段，如果你有特殊的日期解析需求，這裡可能需要調整
                                    grade.setDate(gradeDoc.getDate("date"));
                                    return grade;
                                })
                                .collect(Collectors.toList());
                        restaurant.setGrades(grades);
                    }

                    return restaurant;
                })
                .collect().asList()
                .map(fullList -> fullList.stream()
                .limit(20)
                .collect(Collectors.toList()));
    }

    private ReactiveMongoCollection<Document> getReactiveCollection() {
        return reactiveMongoClient.getDatabase("sample_restaurants")
                .getCollection("restaurants");
    }

    // 自行包裝寫法
    public Uni<List<Restaurant>> selfReactiveList() {
        var ctx = Vertx.currentContext();
        var start = System.currentTimeMillis();
        return Multi.createBy().replaying()
                .ofMulti(Multi.createFrom()
                        .publisher(
                                AdaptersToFlow.publisher(
                                        reactiveMongoClient.getDatabase("sample_restaurants").unwrap()
                                                .getCollection("restaurants")
                                                .find())))
                .emitOn(cmd -> ctx.runOnContext(x -> cmd.run()))
                .map(document -> {
                    // 幫我補上map..用doc.getString...getInterage寫法
                    Restaurant restaurant = new Restaurant();
                    restaurant.setBorough(document.getString("borough"));
                    restaurant.setCuisine(document.getString("cuisine"));
                    restaurant.setName(document.getString("name"));

                    Document addressDoc = document.get("address", Document.class);
                    if (addressDoc != null) {
                        Address address = new Address();
                        address.setBuilding(addressDoc.getString("building"));
                        address.setStreet(addressDoc.getString("street"));
                        address.setZipcode(addressDoc.getString("zipcode"));

                        List<Double> coordList = addressDoc.get("coord", List.class);
                        if (coordList != null) {
                            Coord coord = new Coord();
                            coord.setCoordinates(coordList);
                            address.setCoord(coord);
                        }

                        restaurant.setAddress(address);
                    }

                    // 下面是處理 grades 的代碼。因為它是一個複雜的字段，所以我特別為它寫了一段處理邏輯。
                    List<Document> gradesList = document.get("grades", List.class);
                    if (gradesList != null) {
                        List<Grade> grades = gradesList.stream()
                                .map(gradeDoc -> {
                                    Grade grade = new Grade();
                                    grade.setGrade(gradeDoc.getString("grade"));

                                    grade.setScore(Objects.requireNonNullElse(gradeDoc.getInteger("score"), 0));

                                    // 處理 date 字段，如果你有特殊的日期解析需求，這裡可能需要調整
                                    grade.setDate(gradeDoc.getDate("date"));
                                    return grade;
                                })
                                .collect(Collectors.toList());
                        restaurant.setGrades(grades);
                    }

                    return restaurant;
                })
                .collect()
                .asList()
                .onItem().invoke(res -> logger.info("Reactive took " + (System.currentTimeMillis() - start) + " ms"))
                .onFailure().invoke(err -> logger.error("An error occurred", err));
    }


    private MongoCollection<Document> getCollection(){
        return mongoClient.getDatabase("sample_restaurants")
                .getCollection("restaurants");
    }
}
