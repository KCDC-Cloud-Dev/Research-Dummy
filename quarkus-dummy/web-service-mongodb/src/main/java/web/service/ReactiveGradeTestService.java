package web.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.mongodb.reactive.ReactiveMongoClient;
import io.quarkus.mongodb.reactive.ReactiveMongoCollection;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bson.Document;
import web.infrastructure.entity.Grade;

import java.io.IOException;
import java.util.List;

/**
 * @author : Mario.Yu
 * @description :
 * @date : 2023/10/6 上午 11:06
 */
@ApplicationScoped
public class ReactiveGradeTestService {

    @Inject
    ReactiveMongoClient mongoClient;

    public Uni<Void> add(Grade grade){

        var document = new Document()
                .append("grade", grade.getGrade())
                .append("score", grade.getScore())
                .append("date", grade.getDate());


        return getCollection().insertOne(document)
                // 當你從資料庫的插入操作收到一個項目（也就是操作成功或失敗的訊息）時，進行下一步
                .onItem()
                // 不在乎這個操作返回什麼，只在乎它完成了
                .ignore()
                // 無論如何，就算忽略了返回的項目，讓整個流程繼續下去，並返回一個 null 作為結果
                .andContinueWithNull()
                .onFailure().invoke(err -> {
                    // 在這裡記錄你的錯誤日誌
                    System.err.println("錯誤發生: " + err.getMessage());
                });
    }
    public Uni<List<Grade>> list(){
        var objectMapper = new ObjectMapper();
        return getCollection().find()
                .map(doc -> {
                    try {
                        return objectMapper.readValue(doc.toJson(), Grade.class);
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
                }).collect().asList();

//        return getCollection().find()
//                .map(doc -> {
//                    var grade = new Grade();
//                    grade.setGrade(doc.getString("grade"));
//                    grade.setScore(doc.getInteger("score"));
//                    grade.setDate(doc.getDate("date"));
//                    return grade;
//                }).collect().asList();
    }
    private ReactiveMongoCollection<Document> getCollection() {
        return mongoClient.getDatabase("sample")
                .getCollection("simple");
    }
}
