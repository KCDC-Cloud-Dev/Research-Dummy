import http from 'k6/http';
import { check, sleep, group } from 'k6';

export let options = {
    vus: 5, // 5 user
    duration: '2s', // 持續 2 sec
};

export default function () {
    group('Blocking API', function () {
        let address = 'harder9527@gmail.com';

        // 啟動阻塞並測量時間
        let start = new Date().getTime();
        let response = http.get(`http://localhost:8080/mail/single?address=${address}`);
        let end = new Date().getTime();
        let responseTime = end - start;

        // 檢查是否成功
        check(response, {
            'status is 200': (r) => r.status === 200,
        });

        // 输出响应时间和阻塞信息
        console.log(`Blocking API - Response Time: ${responseTime} ms`);
    });

    sleep(1); // 1秒的休眠
}
