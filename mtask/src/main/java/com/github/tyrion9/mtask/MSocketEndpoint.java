package com.github.tyrion9.mtask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@ServerEndpoint(value="/mtasks/{mtaskCode}")
public class MSocketEndpoint {

    private static final Logger log = LoggerFactory.getLogger(MSocketEndpoint.class);

    private static final Map<String, Map<String, Session>> mtaskCodeSessions = new LinkedHashMap<>();

    private MTaskManager mTaskManager;

    public MSocketEndpoint(){
        this.mTaskManager = SpringContextUtil.getMTaskManager();
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("mtaskCode") String mtaskCode) {
        log.debug("onOpen " + session.getId());

        addSession(mtaskCode, session);
    }

    @OnClose
    public void onClose(Session session) {
        log.debug("onClose " + session.getId());

        removeSession(session);
    }

    @OnError
    public void onError(Throwable t) {
        log.error(t.getMessage(), t);
    }

    public static void multicastMsg(String mtaskCode, String msg){
        if (!mtaskCodeSessions.containsKey(mtaskCode))
            return;

        if (mtaskCodeSessions.get(mtaskCode) == null)
            return;

        for(Session session : mtaskCodeSessions.get(mtaskCode).values()) {
            try {
                synchronized (session) {
                    session.getBasicRemote().sendText(msg);
                }
            } catch (IOException e) {
                e.printStackTrace();
                log.error(e.getMessage(), e);
            }
        }
    }

    private void addSession(String mtaskCode, Session session) {
        if (!mTaskManager.existMTaskCode(mtaskCode)) {
            try {
                session.close(new CloseReason(CloseReason.CloseCodes.CANNOT_ACCEPT, "mtaskCode is not found"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (!mtaskCodeSessions.containsKey(mtaskCode))
            mtaskCodeSessions.put(mtaskCode, new LinkedHashMap<>());

        Map<String, Session> sessions = mtaskCodeSessions.get(mtaskCode);
        sessions.put(session.getId(), session);

        log.debug("mtaskCode {} manage session {}", mtaskCode, session.getId());
    }

    private void removeSession(Session session) {
        for(Map.Entry<String, Map<String, Session>> entryTaskCodeSessions : mtaskCodeSessions.entrySet()) {
            String taskCode = entryTaskCodeSessions.getKey();
            Map<String, Session> sessions = entryTaskCodeSessions.getValue();

            for(Map.Entry entry : entryTaskCodeSessions.getValue().entrySet()) {
                if (entry.getKey().equals(session.getId())) {
                    sessions.remove(session.getId());

                    log.debug("remove mtaskcode {} session {}", taskCode,  session.getId());
                    return;
                }
            }
        }
    }
}
