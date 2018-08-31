package com.itors.util.sftp;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;

public class SftpUtil {
	
	private static Log log = LogFactory.getLog(SftpUtil.class);
	private static  String host;
	private static String port ;
	private static String userName ;
	private static String password ;
	private static String timeout;
	private Session session = null;
	private Channel channel = null;
	/**
	 * 上传文件  
	 * @param src 路径
	 * @param dst
	 * @param sftpDetails
	 * @throws Exception
	 */
	public static void uploadFile(String src, String dst,
			Map<String, String> sftpDetails) throws Exception {
		SftpUtil sftpUtil = new SftpUtil();
		ChannelSftp chSftp = sftpUtil.getChannel();
		chSftp.put(src, dst, new MyProgressMonitor(), ChannelSftp.RESUME); // 代码段2
		chSftp.quit();
		sftpUtil.closeChannel();
	}

	/**
	 * 上传文件
	 * @param in 输入流
	 * @param dst
	 * @param fileMD5
	 * @throws Exception
	 */
	public static void uploadFileWithStram(InputStream in, String dst,
			String fileMD5) throws Exception {
		SftpUtil sftpUtil = new SftpUtil();
		ChannelSftp chSftp = sftpUtil.getChannel();
		SftpATTRS attr = null;
		long alreayInSize = 0L;
		try {
			attr = chSftp.stat(fileMD5);
			alreayInSize = attr.getSize();
		} catch (Exception var4) {
			if (var4.getMessage().toLowerCase().equals("no such file")) {
				log.info("上传新文件");
			}
		}
		chSftp.put(in, fileMD5, new MyProgressMonitor(in.available(),
				alreayInSize, fileMD5), ChannelSftp.RESUME);
		chSftp.quit();
		sftpUtil.closeChannel();
	}

	/**
	 * 根据ip，用户名及密码得到一个SFTP
	 * channel对象，即ChannelSftp的实例对象，在应用程序中就可以使用该对象来调用SFTP的各种操作方法
	 * 
	 * @param sftpDetails
	 * @param timeout
	 * @return
	 * @throws JSchException
	 */
	public  ChannelSftp getChannel() throws JSchException {
		log.info("开始连接SFTP host:"+host+":"+port);
		int ftpPort = Integer.parseInt(port);
		if (port != null && !port.equals("")) {
			ftpPort = Integer.valueOf(port);
		}
		JSch jsch = new JSch(); // 创建JSch对象
		session = jsch.getSession(userName, host, ftpPort); // 根据用户名，主机ip，端口获取一个Session对象
		if (password != null) {
			session.setPassword(password); // 设置密码
		}
		Properties config = new Properties();
		config.put("StrictHostKeyChecking", "no");
		session.setConfig(config); // 为Session对象设置properties
		session.setTimeout(Integer.parseInt(timeout)); // 设置timeout时间
		session.connect(5000); // 通过Session建立链接
		channel = session.openChannel("sftp"); // 打开SFTP通道
		channel.connect(); // 建立SFTP通道的连接
		log.info("成功连接SFTP："+host);
		return (ChannelSftp) channel;
	}

	public void closeChannel() throws Exception {
		if (channel != null) {
			channel.disconnect();
		}
		if (session != null) {
			session.disconnect();
		}
	}
	
	/**
	 * 测试程序
	 * 
	 * @param arg
	 * @throws Exception
	 */
	public static void main(String[] arg) throws Exception {
		// 设置主机ip，端口，用户名，密码
		// 测试文件上传
		String src = "D:\\Tool\\A压缩包\\apache-tomcat-7.0.86.zip"; // 本地文件名
		String dst = "/2244.exe"; // 目标文件名
		InputStream in = new FileInputStream(src);
		//uploadFileWithStram(in, dst, "AA");
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getTimeout() {
		return timeout;
	}

	public void setTimeout(String timeout) {
		this.timeout = timeout;
	}
	
	
	
	
}