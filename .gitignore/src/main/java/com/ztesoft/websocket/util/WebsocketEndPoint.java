package com.ztesoft.websocket.util;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@ServerEndpoint("/websocket/{batchKey}")
public class WebsocketEndPoint {
    private static Log log = LogFactory.getLog(WebsocketEndPoint.class);

    @OnOpen
    public void onOpen(@PathParam("batchKey") String batchKey, Session session) {
        log.info("Websocket Start Connecting:" + batchKey);
        WebsocketSessionUtils.put(batchKey, session);
    }

    @OnMessage
    public String onMessage(@PathParam("batchKey") String batchKey,
            String message) {
        return "Got your message (" + message + ").Thanks !";
    }

    @OnError
    public void onError(@PathParam("batchKey") String batchKey,
            Throwable throwable, Session session) {
        log.info("Websocket Connection Exception:" + batchKey);
        log.info(throwable.getMessage(), throwable);
        WebsocketSessionUtils.remove(batchKey);
    }

    @OnClose
    public void onClose(@PathParam("batchKey") String batchKey, Session session) {
        log.info("Websocket Close Connection:" + batchKey);
        WebsocketSessionUtils.remove(batchKey);
    }

}