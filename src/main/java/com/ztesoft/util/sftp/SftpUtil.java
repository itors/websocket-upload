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
		uploadFileWithStram(in, dst, "AA");
	}

	public static void uploadFile(String src, String dst,
			Map<String, String> sftpDetails) throws Exception {
		SftpUtil sftpUtil = new SftpUtil();
		ChannelSftp chSftp = sftpUtil.getChannel(sftpDetails, 60000);
		/**
		 * 代码段1/代码段2/代码段3分别演示了如何使用JSch的不同的put方法来进行文件上传。这三段代码实现的功能是一样的，
		 * 都是将本地的文件src上传到了服务器的dst文件
		 */
		/**
		 * 代码段1 OutputStream out = chSftp.put(dst,new MyProgressMonitor2(),
		 * ChannelSftp.OVERWRITE); // 使用OVERWRITE模式 byte[] buff = new byte[1024
		 * * 256]; // 设定每次传输的数据块大小为256KB int read; if (out != null) {
		 * InputStream is = new FileInputStream(src); do { read = is.read(buff,
		 * 0, buff.length); if (read > 0) { out.write(buff, 0, read); }
		 * out.flush(); } while (read >= 0); }
		 **/

		// 使用这个方法时，dst可以是目录，当dst是目录时，上传后的目标文件名将与src文件名相同
		// ChannelSftp.RESUME：断点续传
		chSftp.put(src, dst, new MyProgressMonitor(), ChannelSftp.RESUME); // 代码段2
		// 将本地文件名为src的文件输入流上传到目标服务器，目标文件名为dst。
		// chSftp.put(new FileInputStream(src), dst,new MyProgressMonitor2(),
		// ChannelSftp.OVERWRITE); // 代码段3

		chSftp.quit();
		sftpUtil.closeChannel();
	}

	public static void uploadFileWithStram(InputStream in, String dst,
			String fileMD5) throws Exception {
		SftpUtil sftpUtil = new SftpUtil();
		ChannelSftp chSftp = sftpUtil.getChannel(sftpDetails, 60000);
		// 使用这个方法时，dst可以是目录，当dst是目录时，上传后的目标文件名将与src文件名相同
		// ChannelSftp.RESUME：断点续传
		// TODO 获取已上传文件的大小
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
				alreayInSize, fileMD5), ChannelSftp.RESUME); // 代码段2
		// 将本地文件名为src的文件输入流上传到目标服务器，目标文件名为dst。
		// chSftp.put(new FileInputStream(src), dst,new MyProgressMonitor2(),
		// ChannelSftp.OVERWRITE); // 代码段3
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
		JSch jsch = new JSch(); // 创建JSch对象
		session = jsch.getSession(ftpUserName, ftpHost, ftpPort); // 根据用户名，主机ip，端口获取一个Session对象
		if (ftpPassword != null) {
			session.setPassword(ftpPassword); // 设置密码
		}
		Properties config = new Properties();
		config.put("StrictHostKeyChecking", "no");
		session.setConfig(config); // 为Session对象设置properties
		session.setTimeout(timeout); // 设置timeout时间
		session.connect(5000); // 通过Session建立链接
		channel = session.openChannel("sftp"); // 打开SFTP通道
		channel.connect(); // 建立SFTP通道的连接
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