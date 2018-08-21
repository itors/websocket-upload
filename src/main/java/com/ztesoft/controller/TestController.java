package com.ztesoft.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ztesoft.websocket.util.WebsocketApp;

@Controller
public class TestController {
   
	@ResponseBody
	@RequestMapping("/test")
	public Object test(String fileId,String msg){
		WebsocketApp.send(fileId, msg);
		
		return true;
	}
	
    
}