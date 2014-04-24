package org.platform.utils.common.sftp;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.platform.utils.common.properties.PropertiesUtils;
import org.platform.utils.common.resource.ResourceManager;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class SFTPUtils {

	private static final Logger logger = Logger.getLogger(SFTPUtils.class);
	
	public static final String SFTP_HOST = "sftp.host";
	public static final String SFTP_PORT = "sftp.port";
	public static final String SFTP_USERNAME = "sftp.username";
	public static final String SFTP_PASSWORD = "sftp.password";
	public static final int SFTP_DEFAULT_PORT = 22;
	public static final String SFTP_LOCATION = "sftp.location";
	
	private static final Map<String, String> defaultSftpDetails = new HashMap<String, String>();

	private static Session globalSession = null;
	
	static {
		Properties properties = PropertiesUtils.obtainValues("sftp/sftp.properties");
		defaultSftpDetails.put(SFTP_HOST, properties.getProperty(SFTP_HOST));
		defaultSftpDetails.put(SFTP_PORT, properties.getProperty(SFTP_PORT));
		defaultSftpDetails.put(SFTP_USERNAME, properties.getProperty(SFTP_USERNAME));
		defaultSftpDetails.put(SFTP_PASSWORD, properties.getProperty(SFTP_PASSWORD));
		openSession();
	}
	
	public static void openSession() {
		openSession(defaultSftpDetails, 10000);
	}
	
	public static void openSession(Map<String, String> sftpDetails, int timeout) {
		String sftpHost = sftpDetails.get(SFTP_HOST);
		String sftpPort = sftpDetails.get(SFTP_PORT);
		String sftpUserName = sftpDetails.get(SFTP_USERNAME);
		String sftpPassword = sftpDetails.get(SFTP_PASSWORD);
		int ftpPort = SFTP_DEFAULT_PORT;
		if (null != sftpPort && !"".equals(sftpPort)) {
			ftpPort = Integer.parseInt(sftpPort);
		}
		try {
			JSch jsch = new JSch(); 
			jsch.addIdentity(ResourceManager.getAbsolutePath("sftp/id_rsa"));
			/** 根据用户名，主机ip，端口获取一个Session对象*/
			globalSession = jsch.getSession(sftpUserName, sftpHost, ftpPort); 
			logger.info("Session Create Finish");
			if (sftpPassword != null) {
				globalSession.setPassword(sftpPassword);
			}
			Properties config = new Properties();
			config.put("StrictHostKeyChecking", "no");
			globalSession.setConfig(config); // 为Session对象设置properties
			globalSession.setTimeout(timeout); // 设置timeout时间
			globalSession.connect(); // 通过Session建立链接
			logger.info("Session Connect Success");
		} catch (JSchException e) {
			logger.info(e.getMessage(), e);
		}
	}
	
	public static void closeSession() {
		if (null != globalSession) {
			globalSession.disconnect();
		}
	}
	
	public static ChannelSftp obtainChannel() {
		Channel channel = null;
		try {
			if (null == globalSession) {
				openSession();
			}
			channel = globalSession.openChannel("sftp"); // 打开SFTP通道
			channel.connect(); // 建立SFTP通道的连接
			logger.info("Channel Connect Success");
		} catch (JSchException e) {
			logger.info(e.getMessage(), e);
		}
		return (ChannelSftp) channel;
	}
	
	public static void closeChannel(ChannelSftp channel) {
		if (channel != null) {
			channel.disconnect();
		}
	}
	
}
