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
	private long count = 0; // ��ǰ���յ����ֽ���
	private long max = 0; // �����ļ���С
	private long percent = -1; // ����
	private String fileMD5;
	
	public MyProgressMonitor(){
		
	}
	public MyProgressMonitor(long max,long alreayInSize,String fileMD5){
		this.max = max;
		this.fileMD5 = fileMD5;
		this.count = alreayInSize;
	}

	/**
	 * ��ÿ�δ�����һ�����ݿ�󣬵���count������count�����Ĳ���Ϊ��һ�δ�������ݿ��С
	 */
	 public boolean count(long count) {
		 //����ͣ
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
	 * ���������ʱ������end����
	 */
	public void end() {
		System.out.println("Transferring done.");
	}

	/**
	 * ���ļ���ʼ����ʱ������init����
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