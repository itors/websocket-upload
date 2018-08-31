package com.itors.util.websocket;

import java.io.IOException;

import javax.websocket.ClientEndpoint;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@ClientEndpoint
public class WebsocketClient {
	  private static Log log = LogFactory.getLog(WebsocketClient.class);

    @OnOpen
    public void onOpen(Session session) {
    	log.info("Connected to endpoint:" + session.getBasicRemote());
        try {
            session.getBasicRemote().sendText("Hello");
        } catch (IOException ex) {
        	log.error(ex.getMessage());
        }
    }

    @OnMessage
    public void onMessage(String message) {
    	log.info(message);
    }

    @OnError
    public void onError(Throwable t) {
        t.printStackTrace();
    }
}