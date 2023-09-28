package org.acme;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class GreetingResourceTest {

    private static final int NUM_THREADS = 50; // 用來模擬高併發的執行緒數量
    private static final int NUM_REQUESTS_PER_THREAD = 10; // 每個執行緒發起的請求數量

    @Test
    public void testGetAccounts(){
        given()
                .when().get("/Account/GetAccounts")  // 使用您自己的 API 路徑
                .then()
                .statusCode(200)
                .body("size()", is(3))
                .body("[0].userName", is("Louis"))
                .body("[1].userName", is("Clerk"))
                .body("[2].userName", is("Mario"));
    }


    @Test
    public void testHighConcurrencyGetAccounts() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);

        // 使用 CountDownLatch 確保所有請求都完成了
        CountDownLatch latch = new CountDownLatch(NUM_THREADS * NUM_REQUESTS_PER_THREAD);

        for (int i = 0; i < NUM_THREADS; i++) {
            executor.submit(() -> {
                for (int j = 0; j < NUM_REQUESTS_PER_THREAD; j++) {
                    given()
                            .when().get("/Account/GetAccounts")
                            .then()
                            .statusCode(200)
                            .body("size()", is(3))
                            .body("[0].userName", is("Louis"))
                            .body("[1].userName", is("Clerk"))
                            .body("[2].userName", is("Mario"));

                    latch.countDown();
                }
            });
        }

        // 等待所有請求完成
        latch.await();

        // 可以在這裡加入其他的驗證邏輯
    }

    @Test
    public void testHelloEndpoint() {
        given()
          .when().get("/hello")
          .then()
             .statusCode(200)
             .body(is("Hello from RESTEasy Reactive"));
    }

}