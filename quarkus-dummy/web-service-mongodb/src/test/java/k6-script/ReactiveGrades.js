import http from 'k6/http';
import { check } from 'k6';

export let options = {
  vus: 100, // 设置虚拟用户数为1
  duration: '1s', // 设置测试持续时间为1秒
};

export default function () {
  let url = 'http://localhost:8080/ReactiveGradeTest/Grades';

  // 记录开始时间
  let startTime = new Date().getTime();

  // 执行 GET 请求
  let response = http.get(url, {
    headers: { 'accept': 'application/json' }
  });

  // 记录结束时间
  let endTime = new Date().getTime();

  // 计算响应时间
  let responseTime = endTime - startTime;

  // 检查响应是否成功
  check(response, {
    'status is 200': (r) => r.status === 200,
  });

  // 输出响应时间
  console.log(`Response Time: ${responseTime} ms`);
}
