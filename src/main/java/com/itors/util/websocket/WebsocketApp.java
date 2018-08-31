package com.itors.util.websocket;

import java.io.IOException;
import java.net.URI;

import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class WebsocketApp {
	private static Log log = LogFactory.getLog(WebsocketApp.class);
    private static String webUrl = "localhost:8080/websocket-upload";
    
    public static void send(String batchKey, String message) {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        String uri = "ws://"+webUrl+"/websocket/" + batchKey;
        log.info("Connecting to" + uri);
        try {
            Session session = WebsocketSessionUtils.get(batchKey);
            if (session == null) {
                session = container.connectToServer(WebsocketClient.class, URI.create(uri));
                WebsocketSessionUtils.put(batchKey, session);
            }
            session.getBasicRemote().sendText(message);
        } catch (DeploymentException ex) {
        	log.error(ex.getMessage());
        } catch (IOException e) {
        	log.error(e.getMessage());
        }
    }
    public static String getWebUrl() {
        return webUrl;
    }

    public static void setWebUrl(String webUrl) {
        WebsocketApp.webUrl = webUrl;
    }
    
    
}