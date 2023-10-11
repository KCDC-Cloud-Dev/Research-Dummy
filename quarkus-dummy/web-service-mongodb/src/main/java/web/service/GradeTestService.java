package web.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bson.Document;
import web.infrastructure.entity.Grade;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : Mario.Yu
 * @description :
 * @date : 2023/10/11 下午 03:26
 */
@ApplicationScoped
public class GradeTestService {

    @Inject
    MongoClient mongoClient;

    public List<Grade> list() {
        List<Grade> list = new ArrayList<>();
        MongoCursor<Document> cursor = getCollection().find().iterator();
        var objectMapper = new ObjectMapper();
        try {
            while (cursor.hasNext()) {
                Document document = cursor.next();
                var grade = objectMapper.readValue(document.toJson(), Grade.class);
                list.add(grade);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } finally {
            cursor.close();
        }
        return list;
    }

    private MongoCollection getCollection() {
        return mongoClient.getDatabase("sample").getCollection("simple");
    }
}
