# Managed Task (MTask)

Tiến trình chạy ngầm là bắt buộc với tất cả các ứng dụng cho các doanh nghiệp. 

Spring Framework có nhiều các thư viện: Spring Scheduling, Spring Boot Quartz...

**MTask** dựa trên nền tảng của Spring Boot phát triển thêm các tính năng kế từ thư viện FPTThread.
- Cho phép đặt lịch (Spring Scheduling theo fixedrate hoặc cron)
- Stop/Start tiến trình ngầm (Restful API)
- Thay đổi tham số (Restful API) 
- Xem log real time (Websocket)

## Cách dùng
Xem chi tiết trong *mtask-sample* 
### 1. Import thư viện:
```maven
    <dependency>
        <groupId>com.github.tyrion9</groupId>
        <artifactId>mtask-spring-boot-starter</artifactId>
        <version>0.0.1-SNAPSHOT</version>
    </dependency>
```

### 2. Viết class extends MTask

*HelloWorldMTask*
```java
public class HelloWorldMTask extends MTask {
    private static final Logger log = LoggerFactory.getLogger(HelloWorldMTask.class);

    @Override
    public void run() {
        log.info("Hello World"); 

        mlog.log("Hello World");  // Websocket Log
    }
}
```

*ComplexMTask*: khai báo tham số, autowired bean để sử dụng
```java
public class ComplexMTask extends MTask {
    private static final Logger log = LoggerFactory.getLogger(ComplexMTask.class);

    @Autowired
    private GreetingService greetingService;

    @MTaskParam("name")
    private String name;

    @Override
    public void run() {
        String greetingMsg = greetingService.greeting(name);

        log.info(greetingMsg);
        mlog.log(greetingMsg); // websocket log
    }
}
```

### 3. Khai báo cấu hình (yaml)
```yaml
-   code: helloworld
    scheduled:
        period: 1000
    name: Hello World MTask
    className: sample.sample1.HelloWorldMTask
    autoStart: true
-   code: complex
    scheduled:
        period: 1000
    name: Autowired Param MTask
    className: sample.sample2.ComplexMTask
    params:
        name: HoaiPN
    autoStart: true

```

### 4. Run
```java
@SpringBootApplication
@ComponentScan(basePackages = {"com.github.tyrion9.mtask", "sample"})
public class SampleApplication {
    public static void main(String[] args) {
        SpringApplication.run(SampleApplication.class, args);
    }
}
```

### 5. Monitor ứng dụng
***Client quản lý chưa được phát triển nên test thử bằng các tool sau:***
 - Postman để call API quản lý (stop/start/thay đổi tham số)
 - Simple Websocket Client - Chrome Plugin - để xem realtime log
 
*Real time log*
![websocket-helloworld](https://user-images.githubusercontent.com/30858651/52554017-1d77e400-2e18-11e9-921d-87245e5ba8e8.PNG)

![websocket-complex](https://user-images.githubusercontent.com/30858651/52554019-1e107a80-2e18-11e9-930e-fc9840df071a.PNG)

*Quản lý list/stop/start/thay đổi tham số*
```
curl -X GET http://localhost:8080/api

curl -X POST http://localhost:8080/api/helloworld/stop

curl -X POST http://localhost:8080/api/helloworld/start
```
![rest-list](https://user-images.githubusercontent.com/30858651/52554018-1e107a80-2e18-11e9-902b-68365652043b.PNG)
