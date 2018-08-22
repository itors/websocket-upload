package com.ztesoft.util.sftp;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
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
	Session session = null;
	Channel channel = null;
	public static final String SFTP_REQ_HOST = "host";
	public static final String SFTP_REQ_PORT = "port";
	public static final String SFTP_REQ_USERNAME = "username";
	public static final String SFTP_REQ_PASSWORD = "password";
	public static final String SFTP_REQ_HOST_VALUE = "192.168.40.1";
	public static final String SFTP_REQ_PORT_VALUE = "22";
	public static final String SFTP_REQ_USERNAME_VALUE = "admin";
	public static final String SFTP_REQ_PASSWORD_VALUE = "123";
	public static final int SFTP_DEFAULT_PORT = 22;
	public static Map<String, String> sftpDetails = null;

	static {
		sftpDetails = new HashMap<String, String>();
		sftpDetails.put(SFTP_REQ_HOST, SFTP_REQ_HOST_VALUE);
		sftpDetails.put(SFTP_REQ_USERNAME, SFTP_REQ_USERNAME_VALUE);
		sftpDetails.put(SFTP_REQ_PASSWORD, SFTP_REQ_PASSWORD_VALUE);
		sftpDetails.put(SFTP_REQ_PORT, "22");
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
		uploadFileWithStram(in, dst, "AA");
	}

	public static void uploadFile(String src, String dst,
			Map<String, String> sftpDetails) throws Exception {
		SftpUtil sftpUtil = new SftpUtil();
		ChannelSftp chSftp = sftpUtil.getChannel(sftpDetails, 60000);
		/**
		 * �����1/�����2/�����3�ֱ���ʾ�����ʹ��JSch�Ĳ�ͬ��put�����������ļ��ϴ��������δ���ʵ�ֵĹ�����һ���ģ�
		 * ���ǽ����ص��ļ�src�ϴ����˷�������dst�ļ�
		 */
		/**
		 * �����1 OutputStream out = chSftp.put(dst,new MyProgressMonitor2(),
		 * ChannelSftp.OVERWRITE); // ʹ��OVERWRITEģʽ byte[] buff = new byte[1024
		 * * 256]; // �趨ÿ�δ�������ݿ��СΪ256KB int read; if (out != null) {
		 * InputStream is = new FileInputStream(src); do { read = is.read(buff,
		 * 0, buff.length); if (read > 0) { out.write(buff, 0, read); }
		 * out.flush(); } while (read >= 0); }
		 **/

		// ʹ���������ʱ��dst������Ŀ¼����dst��Ŀ¼ʱ���ϴ����Ŀ���ļ�������src�ļ�����ͬ
		// ChannelSftp.RESUME���ϵ�����
		chSftp.put(src, dst, new MyProgressMonitor(), ChannelSftp.RESUME); // �����2
		// �������ļ���Ϊsrc���ļ��������ϴ���Ŀ���������Ŀ���ļ���Ϊdst��
		// chSftp.put(new FileInputStream(src), dst,new MyProgressMonitor2(),
		// ChannelSftp.OVERWRITE); // �����3

		chSftp.quit();
		sftpUtil.closeChannel();
	}

	public static void uploadFileWithStram(InputStream in, String dst,
			String fileMD5) throws Exception {
		SftpUtil sftpUtil = new SftpUtil();
		ChannelSftp chSftp = sftpUtil.getChannel(sftpDetails, 60000);
		// ʹ���������ʱ��dst������Ŀ¼����dst��Ŀ¼ʱ���ϴ����Ŀ���ļ�������src�ļ�����ͬ
		// ChannelSftp.RESUME���ϵ�����
		// TODO ��ȡ���ϴ��ļ��Ĵ�С
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
				alreayInSize, fileMD5), ChannelSftp.RESUME); // �����2
		// �������ļ���Ϊsrc���ļ��������ϴ���Ŀ���������Ŀ���ļ���Ϊdst��
		// chSftp.put(new FileInputStream(src), dst,new MyProgressMonitor2(),
		// ChannelSftp.OVERWRITE); // �����3
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
	public ChannelSftp getChannel(Map<String, String> sftpDetails, int timeout)
			throws JSchException {
		String ftpHost = sftpDetails.get(SFTP_REQ_HOST);
		String port = sftpDetails.get(SFTP_REQ_PORT);
		String ftpUserName = sftpDetails.get(SFTP_REQ_USERNAME);
		String ftpPassword = sftpDetails.get(SFTP_REQ_PASSWORD);
		int ftpPort = SFTP_DEFAULT_PORT;
		if (port != null && !port.equals("")) {
			ftpPort = Integer.valueOf(port);
		}
		JSch jsch = new JSch(); // ����JSch����
		session = jsch.getSession(ftpUserName, ftpHost, ftpPort); // �����û���������ip���˿ڻ�ȡһ��Session����
		if (ftpPassword != null) {
			session.setPassword(ftpPassword); // ��������
		}
		Properties config = new Properties();
		config.put("StrictHostKeyChecking", "no");
		session.setConfig(config); // ΪSession��������properties
		session.setTimeout(timeout); // ����timeoutʱ��
		session.connect(5000); // ͨ��Session��������
		channel = session.openChannel("sftp"); // ��SFTPͨ��
		channel.connect(); // ����SFTPͨ��������
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
}