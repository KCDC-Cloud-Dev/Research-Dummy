package web.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.mongodb.reactive.ReactiveMongoClient;
import io.quarkus.mongodb.reactive.ReactiveMongoCollection;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bson.Document;
import web.infrastructure.entity.Address;
import web.infrastructure.entity.Coord;
import web.infrastructure.entity.Grade;
import web.infrastructure.entity.Restaurant;

import java.io.IOException;
import java.util.ArrayList;
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

    @Inject
    ReactiveMongoClient mongoClient;

    public Uni<List<Restaurant>> list(){
        var objectMapper = new ObjectMapper();
        return getCollection().find()
                .map(doc -> {
                    // 幫我補上map..用doc.getString...getInterage寫法
                    Restaurant restaurant = new Restaurant();
                    restaurant.setBorough(doc.getString("borough"));
                    restaurant.setCuisine(doc.getString("cuisine"));
                    restaurant.setName(doc.getString("name"));

                    Document addressDoc = doc.get("address", Document.class);
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
                    List<Document> gradesList = doc.get("grades", List.class);
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
                }).collect().asList();
                //.map(fullList -> fullList.stream().limit(5).collect(Collectors.toList()));
    }


    private ReactiveMongoCollection<Document> getCollection() {
        return mongoClient.getDatabase("sample_restaurants")
                .getCollection("restaurants");
    }


}
