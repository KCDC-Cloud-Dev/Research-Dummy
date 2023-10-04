# sendmail-sample

使用 SendGrid 服務發送信件
並用 Quarkus 架構串接來發送信件

## SendGrid

提供第三方的 SMTP 服務，每月有提供一定額度的免費，也免綁定信用卡，但註冊的時候要注意：

**對第三方的免費郵件有限制，使用 Gmail 註冊基本上是會馬上被鎖定**

客服會要你提供公司證明之類的（應該是避免濫用），但用公司信箱 (`NTxxxxx@cathaybk.com.tw`）就不會有這個問題，所以如果借不到測試帳號，就用公司信箱申請一個

在裡面的設定可以產生一個 `API_KEY`，這個金鑰是用來做連線的

## Sample Code

設定在 `resources/application.properties` 中，

```java
# 設定登入方式為 API_KEY
sendgrid.username=apikey 
sendgrid.password=<API_KEY> // 替換成你的 Key， Key 的用法請參考下面執行說明

quarkus.mailer.from=<FROM> // 替換成你要顯示的寄件人
```

[參數說明](https://quarkus.io/guides/mailer-reference#sendgrid)

範例寫在 `MailResource.java` 中，是使用 RESTful API GET，並需入一個 mail address 為參數:

```Java
@GET
@Blocking
public void sendEmail(@QueryParam("addr：ess") String address) {
    mailer.send(
        Mail.withText(address, "Ahoy from Quarkus", "A simple email sent from a Quarkus application.")
    );
}
```

執行以下指令可以做測試

## 測試

測試時要加入 API_KEY 有兩個使用方法：

1. 直接寫在 `application.properties` 中（不建議）
    `
    quarkus.mailer.password=<API_KEY>
    `

    然後執行

    ```bash
    ./gradlew --console=plain quarkusDev
    ```

2. 執行服務時以參數帶入
    `/gradlew --console=plain quarkusDev -Dquarkus.mailer.password=<API_KEY>`

目前還在測試包成 Docker Image 後直接取用環境變數是否可行

然後使用這個指令，就可以發送信件：

`curl http://localhost:8080/mail?address=XXX@gmail.com`
