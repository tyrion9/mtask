# Managed Task (MTask)

Background task is required for every enterprise projects.

Spring Framework support many kind of background tasks: Spring Scheduling, Spring Boot Quartz...

**MTask** is based on Spring Boot add new feature for monitoring and controlling on the fly.
- Add/remove Scheduling Task via Rest API (Spring Scheduling theo fixedrate hoặc cron)
- Stop/start scheduling task via API (Restful API)
- Change parameter's tasks via API (Restful API) 
- Monitoring task by realtime log via Websocket

## Step by step
More info in *mtask-sample* 
### 1. Import maven library:
```maven
        <dependency>
            <groupId>com.github.tyrion9</groupId>
            <artifactId>mtask-spring-boot-starter</artifactId>
            <version>0.0.1</version>
        </dependency>
```

### 2. Write a class extends MTask

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

*ComplexMTask*: declare parameters, autowired bean to use
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

### 3. declare mtask configuration (yaml)
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

### 5. Monitor application
***Client Tool is not developed yet, so use other tool for management:***
 - Postman call rest API for management (stop/start/change parameters)
 - Simple Websocket Client - Chrome Plugin - to check log realtime
 
*Real time log*
![websocket-helloworld](https://user-images.githubusercontent.com/30858651/52554017-1d77e400-2e18-11e9-921d-87245e5ba8e8.PNG)

![websocket-complex](https://user-images.githubusercontent.com/30858651/52554019-1e107a80-2e18-11e9-930e-fc9840df071a.PNG)

*API list/stop/start/change parameters*
```
curl -X GET http://localhost:8080/api

curl -X POST http://localhost:8080/api/helloworld/stop

curl -X POST http://localhost:8080/api/helloworld/start
```
![rest-list](https://user-images.githubusercontent.com/30858651/52554018-1e107a80-2e18-11e9-902b-68365652043b.PNG)
