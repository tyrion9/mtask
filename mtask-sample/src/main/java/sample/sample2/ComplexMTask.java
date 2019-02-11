package sample.sample2;

import com.github.tyrion9.mtask.MTask;
import com.github.tyrion9.mtask.MTaskParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

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
