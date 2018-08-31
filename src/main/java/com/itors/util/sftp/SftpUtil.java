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
	 * �ϴ��ļ�  
	 * @param src ·��
	 * @param dst
	 * @param sftpDetails
	 * @throws Exception
	 */
	public static void uploadFile(String src, String dst,
			Map<String, String> sftpDetails) throws Exception {
		SftpUtil sftpUtil = new SftpUtil();
		ChannelSftp chSftp = sftpUtil.getChannel();
		chSftp.put(src, dst, new MyProgressMonitor(), ChannelSftp.RESUME); // �����2
		chSftp.quit();
		sftpUtil.closeChannel();
	}

	/**
	 * �ϴ��ļ�
	 * @param in ������
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
				log.info("�ϴ����ļ�");
			}
		}
		chSftp.put(in, fileMD5, new MyProgressMonitor(in.available(),
				alreayInSize, fileMD5), ChannelSftp.RESUME);
		chSftp.quit();
		sftpUtil.closeChannel();
	}

	/**
	 * ����ip���û���������õ�һ��SFTP
	 * channel���󣬼�ChannelSftp��ʵ��������Ӧ�ó����оͿ���ʹ�øö���������SFTP�ĸ��ֲ�������
	 * 
	 * @param sftpDetails
	 * @param timeout
	 * @return
	 * @throws JSchException
	 */
	public  ChannelSftp getChannel() throws JSchException {
		log.info("��ʼ����SFTP host:"+host+":"+port);
		int ftpPort = Integer.parseInt(port);
		if (port != null && !port.equals("")) {
			ftpPort = Integer.valueOf(port);
		}
		JSch jsch = new JSch(); // ����JSch����
		session = jsch.getSession(userName, host, ftpPort); // �����û���������ip���˿ڻ�ȡһ��Session����
		if (password != null) {
			session.setPassword(password); // ��������
		}
		Properties config = new Properties();
		config.put("StrictHostKeyChecking", "no");
		session.setConfig(config); // ΪSession��������properties
		session.setTimeout(Integer.parseInt(timeout)); // ����timeoutʱ��
		session.connect(5000); // ͨ��Session��������
		channel = session.openChannel("sftp"); // ��SFTPͨ��
		channel.connect(); // ����SFTPͨ��������
		log.info("�ɹ�����SFTP��"+host);
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
	 * ���Գ���
	 * 
	 * @param arg
	 * @throws Exception
	 */
	public static void main(String[] arg) throws Exception {
		// ��������ip���˿ڣ��û���������
		// �����ļ��ϴ�
		String src = "D:\\Tool\\Aѹ����\\apache-tomcat-7.0.86.zip"; // �����ļ���
		String dst = "/2244.exe"; // Ŀ���ļ���
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