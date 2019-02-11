package com.github.tyrion9.mtask;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MLogger {
    private String mtaskCode;

    public void setMtaskCode(String mtaskCode){
        this.mtaskCode = mtaskCode;
    }

    public void log(String msg) {
        String formatedMsg = DateTimeFormatter.ofPattern("HH:mm:ss.SSS").format(LocalDateTime.now()) + " " + msg;

        MSocketEndpoint.multicastMsg(mtaskCode, formatedMsg);
    }
}
