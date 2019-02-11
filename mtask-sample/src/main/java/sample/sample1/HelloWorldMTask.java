package sample.sample1;

import com.github.tyrion9.mtask.MTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloWorldMTask extends MTask {
    private static final Logger log = LoggerFactory.getLogger(HelloWorldMTask.class);

    @Override
    public void run() {
        log.info("Hello World");

        mlog.log("Hello World");  // Websocket Log
    }
}
