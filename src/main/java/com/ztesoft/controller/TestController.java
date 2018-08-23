package com.ztesoft.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.ztesoft.util.Constant;
import com.ztesoft.util.sftp.SftpUtil;
import com.ztesoft.util.websocket.WebsocketSessionUtils;

@Controller
public class TestController {
   
	@ResponseBody
	@RequestMapping("/upload")
	public Object test(@RequestParam("file") MultipartFile file,@RequestParam("fileKey")String fileKey){
		Map rtnMap = new HashMap();
		rtnMap.put("resultFlag", true);
		rtnMap.put("resultCode", "100001");
		rtnMap.put("msg","上传成功");
		MultipartFile  multipartFile= file;
		String fileKey1 = fileKey;
		try {
			WebsocketSessionUtils.setStatus(fileKey, Constant.CONTINUE);
			SftpUtil.uploadFileWithStram((InputStream)multipartFile.getInputStream(),multipartFile.getOriginalFilename(),fileKey);
			if(Constant.SUSPEND.equals(WebsocketSessionUtils.getStatus(fileKey))){
				rtnMap.put("resultFlag", true);
				rtnMap.put("resultCode", "100002");
				rtnMap.put("msg","已暂停");
			}
		} catch (IOException e) {
			e.printStackTrace();
			rtnMap.put("flag", false);
			rtnMap.put("resultCode", "100003");
			rtnMap.put("msg","上传失败");
		} catch (Exception e) {
			e.printStackTrace();
			rtnMap.put("flag", false);
			rtnMap.put("resultCode", "100003");
			rtnMap.put("msg","上传失败");
		}
		return rtnMap;
	}
	
    
}