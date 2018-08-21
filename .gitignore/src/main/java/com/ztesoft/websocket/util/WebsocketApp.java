package com.ztesoft.websocket.util;

import java.io.IOException;
import java.net.URI;

import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

public class WebsocketApp {
    private static String webUrl = "localhost";
    
    public static void send(String batchKey, String message) {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        String uri = "ws://"+webUrl+"/websocket/" + batchKey;
        System.out.println("Connecting to" + uri);
        try {
            Session session = WebsocketSessionUtils.get(batchKey);
            if (session == null) {
                session = container.connectToServer(WebsocketClient.class, URI.create(uri));
                WebsocketSessionUtils.put(batchKey, session);
            }
            session.getBasicRemote().sendText(message);
        } catch (DeploymentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getWebUrl() {
        return webUrl;
    }

    public static void setWebUrl(String webUrl) {
        WebsocketApp.webUrl = webUrl;
    }
    
    
}