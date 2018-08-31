package com.itors.util.websocket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.Session;

import com.itors.util.Constant;

public class WebsocketSessionUtils {

    public static Map<String, Session> clients = new ConcurrentHashMap<>();
    //做暂停/继续使用
    public static Map<String, String> clientsStatus = new ConcurrentHashMap<>();
    public static void put(String batchKey, Session session) {
        clients.put(batchKey, session);
    }

    public static Session get(String batchKey) {
        return clients.get(batchKey);
    }

    public static void remove(String batchKey) {
        clients.remove(batchKey);
        clientsStatus.remove(batchKey);
    }

    public static boolean hasConnection(String batchKey) {
        return clients.containsKey(batchKey);
    }
    public static String getStatus(String batchKey){
    	return clientsStatus.get(batchKey)==null?Constant.CONTINUE:clientsStatus.get(batchKey);
    }
    public static String setStatus(String batchKey,String status){
    	return clientsStatus.put(batchKey, status);
    }

}