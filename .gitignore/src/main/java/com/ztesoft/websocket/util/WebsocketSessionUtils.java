package com.ztesoft.websocket.util;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.Session;

public class WebsocketSessionUtils {

    public static Map<String, Session> clients = new ConcurrentHashMap<>();

    public static void put(String batchKey, Session session) {
        clients.put(batchKey, session);
    }

    public static Session get(String batchKey) {
        return clients.get(batchKey);
    }

    public static void remove(String batchKey) {
        clients.remove(batchKey);
    }

    public static boolean hasConnection(String batchKey) {
        return clients.containsKey(batchKey);
    }

}