package web.service;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bson.Document;
import web.infrastructure.entity.Address;
import web.infrastructure.entity.Coord;
import web.infrastructure.entity.Grade;
import web.infrastructure.entity.Restaurant;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author : Mario.Yu
 * @description :
 * @date : 2023/10/6 下午 03:36
 */
@ApplicationScoped
public class RestaurantService {

    @Inject
    MongoClient mongoClient;


    public List<Restaurant> list() {
        List<Restaurant> list = new ArrayList<>();
//        MongoCursor<Document> cursor = getCollection().find().limit(20).iterator();
        MongoCursor<Document> cursor = getCollection().find().iterator();
        try {
            while (cursor.hasNext()) {
                Document document = cursor.next();

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

                list.add(restaurant);
            }
        } finally {
            cursor.close();
        }
        return list;
    }

    private MongoCollection getCollection() {
        return mongoClient.getDatabase("sample_restaurants").getCollection("restaurants");
    }
}
