package sample.sample2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class GreetingService {

    public String greeting(String name){
        return "Hi " + name + ", How are you";
    }
}
