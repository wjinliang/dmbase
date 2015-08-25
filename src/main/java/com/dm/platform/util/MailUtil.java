package com.dm.platform.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;

public class MailUtil {
	private static MailUtil instance = new MailUtil();

	private MailUtil() {
	}

	public static MailUtil getInstance() {
		return instance;
	}

	public void sendMail(String toAddr, String subject, String content)
			throws Exception {
		Properties props = new Properties();
		props.setProperty("mail.smtp.auth", "true");
		props.setProperty("mail.transport.protocol", "smtp");

		Session session = Session.getDefaultInstance(props);
		session.setDebug(true);

		Message msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress(ConfigUtil.getConfigContent("mail", "email")));
		msg.setSubject(subject);
		msg.setSentDate(new Date());
		StringBuffer sb = new StringBuffer();
		sb = getEmailTemplate();
		String cStr = sb.toString().replace("${newP@ssword}", content);
		msg.setDataHandler(new DataHandler(new ByteArrayDataSource(cStr, "text/html")));
		Transport trans = session.getTransport();
		trans.connect(ConfigUtil.getConfigContent("mail", "stmp"), Integer.valueOf(ConfigUtil.getConfigContent("mail", "port")), ConfigUtil.getConfigContent("mail", "account"), ConfigUtil.getConfigContent("mail", "password"));
		trans.sendMessage(msg, new Address[] { new InternetAddress(toAddr) });
		trans.close();
	}
	
	private StringBuffer getEmailTemplate() throws Exception{
		String basePath = ConfigUtil.getConfigContent("dm",
				"TomcatPath");
		String filePath = basePath+"/views/template/email.html";
		File file = new File(filePath);
		StringBuffer sb = new StringBuffer();
		if (file.isFile() && file.exists()) {
			InputStreamReader read = new InputStreamReader(
					new FileInputStream(file), "utf8");
			BufferedReader br = new BufferedReader(read);
			String lineTXT = null;
			while ((lineTXT = br.readLine()) != null) {
				sb.append(lineTXT);
			}
			read.close();
		}
		return sb;
	}
}
