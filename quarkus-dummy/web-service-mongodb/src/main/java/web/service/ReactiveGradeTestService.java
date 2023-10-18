package web.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoCollection;
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
import web.infrastructure.entity.Grade;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author : Mario.Yu
 * @description :
 * @date : 2023/10/6 上午 11:06
 */
@ApplicationScoped
public class ReactiveGradeTestService {

    private final static Logger logger = Logger.getLogger(ReactiveRestaurantService.class);

    @Inject
    MongoClient mongoClient;
    @Inject
    ReactiveMongoClient reactiveMongoClient;

    public Uni<Void> add(Grade grade){

        List<Document> documents = IntStream.range(0, 10)  // 生成一個範圍為0-4999的數字流
                .mapToObj(i -> new Document()  // 對每個數字應用一個函數來生成一個Document
                        .append("grade", grade.getGrade())
                        .append("score", grade.getScore())
                        .append("date", grade.getDate()))
                .collect(Collectors.toList());  // 轉換生成的流為List

        return getReactiveCollection().insertMany(documents)
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
    public Uni<Void> bachAdd(Grade grade){


        List<Document> documents = IntStream.range(0, 5000)  // 生成一個範圍為0-4999的數字流
                .mapToObj(i -> new Document()  // 對每個數字應用一個函數來生成一個Document
                        .append("grade", grade.getGrade())
                        .append("score", grade.getScore())
                        .append("date", grade.getDate()))
                .collect(Collectors.toList());  // 轉換生成的流為List

        return getReactiveCollection().insertMany(documents)
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
        return getReactiveCollection().find()
                .map(doc -> {
                    try {
                        return objectMapper.readValue(doc.toJson(), Grade.class);
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
                }).collect().asList();
//                .map(fullList -> fullList.stream().limit(10).collect(Collectors.toList()));
    }

    /**
     * 自行包裝非同步寫法
     * @return List<Grade>
     */
    public Uni<List<Grade>> selfReactiveList() {
        var ctx = Vertx.currentContext();
        var start = System.currentTimeMillis();
        return Multi.createBy().replaying()
                .ofMulti(Multi.createFrom()
                        .publisher(
                                AdaptersToFlow.publisher(
                                        reactiveMongoClient.getDatabase("sample").unwrap()
                                                .getCollection("simple",Grade.class)
                                                .find())))
                .emitOn(cmd -> ctx.runOnContext(x -> cmd.run()))
                .collect()
                .asList()
                .onItem().invoke(res -> logger.info("Reactive took " + (System.currentTimeMillis() - start) + " ms"))

                .onFailure().invoke(err -> logger.error("An error occurred", err));
    }

    private ReactiveMongoCollection<Document> getReactiveCollection() {
        return reactiveMongoClient.getDatabase("sample")
                .getCollection("simple");
    }


    public Multi<Grade> listWithMutiny() {
        MongoCollection<Document> collection = getCollection();
        var objectMapper = new ObjectMapper();
        var ddd = collection.find();


        return Multi.createFrom()
                .publisher(() -> collection.find())
                .onItem().transform(doc -> {
                    try {
                        return objectMapper.readValue(doc.toJson(), Grade.class);
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
                });
    }



    private MongoCollection<Document> getCollection(){
        return mongoClient.getDatabase("sample_restaurants")
                .getCollection("restaurants");
    }
}
