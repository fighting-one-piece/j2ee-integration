package org.platform.utils.mail;

import java.io.File;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.annotation.Resource;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml","classpath:spring/applicationContext-attach.xml",
		"classpath:spring/applicationContext-redis.xml","classpath:spring/applicationContext-mongo.xml"})
@TransactionConfiguration(transactionManager="transactionManager",defaultRollback=false)
@Transactional
public class MailTest {
	
	@Resource(name = "mailSender")
	private JavaMailSender mailSender = null;
	
	@Test
	public void testSendMail() throws Exception {
		JavaMailSenderImpl mailSenderImpl = new JavaMailSenderImpl();
		MimeMessage mimeMessage = mailSenderImpl.createMimeMessage();
		//设置utf-8或GBK编码，否则邮件会有乱码  
		MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "utf-8");
		messageHelper.setFrom("chinasoftione@126.com");
		messageHelper.setTo("chinasoftitwo@126.com");
		messageHelper.setSubject("ChinaSoftiSend");
		messageHelper.setCc("125906088@qq.com");
		messageHelper.setText("Hello,This is a China Softi Mail");
		mailSender.send(mimeMessage);
	}
	
	@Test
	public void testSendMailWithAttach() throws Exception {
		JavaMailSenderImpl mailSenderImpl = new JavaMailSenderImpl();
		MimeMessage mimeMessage = mailSenderImpl.createMimeMessage();
		//设置utf-8或GBK编码，否则邮件会有乱码  
		MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "utf-8");
		messageHelper.setFrom("chinasoftione@126.com");
		messageHelper.setTo("chinasoftitwo@126.com");
		messageHelper.setSubject("ChinaSoftiSend");
		messageHelper.setCc("125906088@qq.com");
		messageHelper.setText("Hello,This is a China Softi Mail");
		messageHelper.setText("<html><head></head><body><h1>hello!!chinasofti</h1></body></html>", true);
		messageHelper.addInline("1", new File("D:\\1.jpg"));
		messageHelper.addInline("2", new File("D:\\2.txt"));
		File file = new File("D:\\3.rar");
		messageHelper.addAttachment(MimeUtility.encodeWord(file.getName()), file);
		mailSender.send(mimeMessage);
	}
	
	@Test
	public void testReveiveMail() throws Exception {
		Properties props = new Properties();  
		//存储接收邮件服务器使用的协议，这里以POP3为例.  
		props.setProperty("mail.store.protocol", "pop3");  
		//设置接收邮件服务器的地址. 
		props.setProperty("mail.pop3.host", "pop.126.com");  
		//根据属性新建一个邮件会话.  
		Session session=Session.getInstance(props);  
		//从会话对象中获得POP3协议的Store对象.  
		Store store = session.getStore("pop3");  
		//如果需要查看接收邮件的详细信息，需要设置Debug标志.  
		session.setDebug(false);  
		//连接邮件服务器  
		store.connect("pop.126.com", "chinasoftitwo@126.com", "@chinasoftitwo");  
		//获取邮件服务器的收件箱  
		Folder folder = store.getFolder("INBOX");  
		//以只读权限打开收件箱  
		folder.open(Folder.READ_ONLY);  
		//创建搜索条件  
//		SearchTerm st=new OrTerm (new FromStringTerm("chinasoftione@126.com"),  
//				new FromStringTerm("chinasoftione@126.com"));  
//		SearchTerm st=new AndTerm(new FromStringTerm("chinasoftione@126.com"),  
//				new ReceivedDateTerm(ComparisonTerm.EQ, new Date()));  
//		Message[] msgs=folder.search(st);  
		
		//获取收件箱中的邮件，也可以使用getMessage(int 邮件的编号)来获取具体某一封邮件  
		Message[] messages = folder.getMessages();  
		for (Message message : messages) {
			System.out.println("from: " + message.getFrom());
			System.out.println("subject: " + message.getSubject());
			System.out.println("contentType: " + message.getContentType());
			Multipart multiPart = (Multipart) message.getContent();
			for (int i = 0, n = multiPart.getCount(); i < n; i++) {
				BodyPart bodyPart = multiPart.getBodyPart(i);
				System.out.println("bodyPart: " + bodyPart.getContent());
				DataHandler dataHandler = bodyPart.getDataHandler();
				System.out.println("datahandler content: " + dataHandler.getContent());
				String disposition = bodyPart.getDisposition();
				System.out.println("disposition: " + disposition);
				if (null != disposition && (Part.INLINE.equals(disposition) || 
						Part.ATTACHMENT.equals(disposition))) {
					System.out.println("fileName: " + bodyPart.getFileName());
				}
			}
			//设置标记  
			//message.setFlag(Flags.Flag.SEEN, true);  
			//message.saveChanges(); 
		}
		//关闭连接
		folder.close(false);  
		store.close();  
	}
}
