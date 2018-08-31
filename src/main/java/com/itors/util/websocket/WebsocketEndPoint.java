package com.itors.util.websocket;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.itors.util.Constant;

@ServerEndpoint("/websocket/{batchKey}")
public class WebsocketEndPoint {
    private static Log log = LogFactory.getLog(WebsocketEndPoint.class);


    @OnOpen
    public void onOpen(@PathParam("batchKey") String batchKey, Session session) {
        log.info("Websocket Start Connecting:" + batchKey);
        WebsocketSessionUtils.put(batchKey, session);
    }

    @OnMessage
    public String onMessage(@PathParam("batchKey") String batchKey,String message, Session session) {
    	//状态修改
    	if(Constant.SUSPEND.endsWith(message)){
    		setSocketStatus(batchKey,message);
    		return null;
    	}
        return "Got your message (" + batchKey + ").Thanks !";
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
    //每个上传状态
    private void setSocketStatus(String batchKey,String status){
    	//添加状态
		WebsocketSessionUtils.setStatus(batchKey,Constant.SUSPEND);
    }

}