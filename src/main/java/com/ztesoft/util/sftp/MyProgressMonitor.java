package com.ztesoft.util.sftp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jcraft.jsch.SftpProgressMonitor;
import com.ztesoft.util.Constant;
import com.ztesoft.util.websocket.WebsocketApp;
import com.ztesoft.util.websocket.WebsocketEndPoint;
import com.ztesoft.util.websocket.WebsocketSessionUtils;

public class MyProgressMonitor implements SftpProgressMonitor {
	
	private static Log log = LogFactory.getLog(WebsocketEndPoint.class);
	private long count = 0; // 当前接收的总字节数
	private long max = 0; // 最终文件大小
	private long percent = -1; // 进度
	private String fileMD5;
	
	public MyProgressMonitor(){
		
	}
	public MyProgressMonitor(long max,long alreayInSize,String fileMD5){
		this.max = max;
		this.fileMD5 = fileMD5;
		this.count = alreayInSize;
	}

	/**
	 * 当每次传输了一个数据块后，调用count方法，count方法的参数为这一次传输的数据块大小
	 */
	 public boolean count(long count) {
		 //已暂停
		 if(Constant.SUSPEND.equals(WebsocketSessionUtils.getStatus(fileMD5))){
			 return false;
		 }
	      this.count += count;
	      if (percent >= this.count * 100 / max) {
	        return true;
	      }
	      percent = this.count * 100 / max;
	      log.info("Completed " + this.count + "(" + percent + "%) out of " + max + ".");
		  WebsocketApp.send(fileMD5, percent+ "%");
	      return true;
	    }

	/**
	 * 当传输结束时，调用end方法
	 */
	public void end() {
		System.out.println("Transferring done.");
	}

	/**
	 * 当文件开始传输时，调用init方法
	 */
	public void init(int op, String src, String dest, long max) {
		if (op == SftpProgressMonitor.PUT) {
			log.info("Upload file begin.");
		} else {
			log.info("Download file begin.");
		}
		//this.max = max;
		//this.count = 0;
		this.percent = -1;
	}
	
	  
}