package cn.slimsmart.mailer.impl;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.slimsmart.mailer.MailService;
import cn.slimsmart.mailer.entity.Mail;
import cn.slimsmart.mailer.exception.MailException;
import cn.slimsmart.mailer.template.MailTemplateGenerator;
import cn.slimsmart.mailer.util.MailUtil;
import cn.slimsmart.mailer.util.ResourceUtil;

/**
 * 发送邮件方法实现类
 * 
 * @author zhutianwei
 * @date 2011-05-29
 * @version v1.0
 */
public class MailServiceImpl implements MailService {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private static final String MAIL_HOST = ResourceUtil.getResources("mail.host");
	private static final String MAIL_USERNAME = ResourceUtil.getResources("mail.userName");
	private static final String MAIL_PASSWORD = ResourceUtil.getResources("mail.password");
	private static final String MAIL_FROM = ResourceUtil.getResources("mail.from");

	private static String MAIL_SMTP_LOCALADDRESS = null;

	static {
		try {
			MAIL_SMTP_LOCALADDRESS = ResourceUtil.getResources("mail.smtp.localaddress");
		} catch (Exception e) {
			MAIL_SMTP_LOCALADDRESS = null;
		}
	}

	private static final String MAIL_CONTENT_HTML_TYPE = "text/html; charset=" + MailUtil.ENCODING;

	public void sendText(String to, String subject, String content) {
		if (null == to || "".equals(to = to.trim())) {
			logger.error("sendText mail to is empty.");
			throw new MailException("sendText mail to is empty.");
		}
		if (null == subject || "".equals(subject = subject.trim())) {
			logger.warn("sendText mail subject is empty.");
		}
		if (null == content || "".equals(content = content.trim())) {
			logger.warn("sendText mail content is empty.");
		}
		Mail mail = new Mail();
		mail.setTo(to);
		mail.setSubject(subject);
		mail.setContent(content);
		mail.setSendDate(new Date());
		mail.setHtml(false);
		mail.setFrom(MAIL_FROM);
		mail.setDebug(false);
		sendMail(mail);
	}

	public void sendText(String to, String subject, String templateName, Map<?, ?> model) {
		if (null == to || "".equals(to = to.trim())) {
			logger.error("sendText mail to is empty.");
			throw new MailException("sendText mail to is empty.");
		}

		if (null == subject || "".equals(subject = subject.trim())) {
			logger.warn("sendText mail subject is empty.");
		}
		String content = "";
		if (null == templateName || "".equals(templateName = templateName.trim())) {
			logger.warn("sendText mail templateName is empty.");
		} else {
			content = MailTemplateGenerator.generateEmailContent(templateName, model);
		}
		Mail mail = new Mail();
		mail.setTo(to);
		mail.setSubject(subject);
		mail.setContent(content);
		mail.setSendDate(new Date());
		mail.setHtml(false);
		mail.setFrom(MAIL_FROM);
		mail.setDebug(false);
		sendMail(mail);
	}

	public void sendHtml(String to, String subject, String content, File... attachementFile) {
		if (null == to || "".equals(to = to.trim())) {
			logger.error("sendText mail to is empty.");
			throw new MailException("sendText mail to is empty.");
		}

		if (null == subject || "".equals(subject = subject.trim())) {
			logger.warn("sendText mail subject is empty.");
		}
		if (null == content || "".equals(content = content.trim())) {
			logger.warn("sendText mail content is empty.");
		}
		Mail mail = new Mail();
		mail.setTo(to);
		mail.setSubject(subject);
		mail.setContent(content);
		mail.setSendDate(new Date());
		mail.setHtml(true);
		mail.setFrom(MAIL_FROM);
		mail.setDebug(false);

		if (null != attachementFile && attachementFile.length > 0) {
			for (File file : attachementFile) {
				mail.addAttachment(file.getName(), file, true);
			}
		}
		sendMail(mail);
	}

	public void sendHtml(String to, String subject, String templateName, Map<?, ?> model, File... attachementFile) {
		if (null == to || "".equals(to = to.trim())) {
			logger.error("sendText mail to is empty.");
			throw new MailException("sendText mail to is empty.");
		}

		if (null == subject || "".equals(subject = subject.trim())) {
			logger.warn("sendText mail subject is empty.");
		}
		String content = "";
		if (null == templateName || "".equals(templateName = templateName.trim())) {
			logger.warn("sendText mail templateName is empty.");
		} else {
			content = MailTemplateGenerator.generateEmailContent(templateName, model);
		}
		Mail mail = new Mail();
		mail.setTo(to);
		mail.setSubject(subject);
		mail.setContent(content);
		mail.setSendDate(new Date());
		mail.setHtml(true);
		mail.setFrom(MAIL_FROM);
		mail.setDebug(false);

		if (null != attachementFile && attachementFile.length > 0) {
			for (File file : attachementFile) {
				mail.addAttachment(file.getName(), file, true);
			}
		}
		sendMail(mail);
	}

	public void sendMail(Mail mail) {
		logger.info("sendMail mail start ......");
		Properties props = System.getProperties();
		
		//常用配置
		props.put("mail.smtp.host", MAIL_HOST);
		props.put("mail.smtp.auth", "true");
		
		//指定邮件源地址
		if(MAIL_SMTP_LOCALADDRESS!=null && !"".equals(MAIL_SMTP_LOCALADDRESS)){
			props.put("mail.smtp.localaddress", MAIL_SMTP_LOCALADDRESS);
		}
		 
		Session session = Session.getInstance(props, null);
		if (mail.isDebug()) {
			session.setDebug(mail.isDebug());
		}
		MimeMessage msg = new MimeMessage(session);
		// 添加from地址
		try {
			msg.setFrom(new InternetAddress(mail.getFrom()));
		} catch (AddressException e) {
			logger.error("sendMail validate fromAddress exception. please,check fromAddress=" + MAIL_FROM);
			throw new MailException("sendMail validate fromAddress exception. please,check fromAddress=" + MAIL_FROM);
		} catch (MessagingException e) {
			logger.error("sendMail add fromAddress messagingException. please,check fromAddress=" + MAIL_FROM);
			throw new MailException("sendMail add fromAddress messagingException. please,check fromAddress=" + MAIL_FROM);
		}
		// 添加to地址
		InternetAddress[] address = null;
		try {
			address = InternetAddress.parse(mail.getTo());
		} catch (AddressException e2) {
			logger.error("sendMail validate toAddress exception. please,check toAddress=" + mail.getTo());
			throw new MailException("sendMail validate toAddress exception. please,check toAddress=" + mail.getTo());
		}
		try {
			msg.setRecipients(Message.RecipientType.TO, address);
		} catch (MessagingException e1) {
			logger.error("sendMail add toAddress messagingException. please,check toAddress=" + mail.getTo());
			throw new MailException("sendMail add toAddress messagingException. please,check toAddress=" + mail.getTo());
		}
		// 添加cc地址
		String cc = mail.getCc();
		if (null != cc && !"".equals(cc = cc.trim())) {
			try {
				address = InternetAddress.parse(cc);
			} catch (AddressException e2) {
				logger.error("sendMail validate ccAddress exception. please,check ccAddress=" + cc);
				throw new MailException("sendMail validate ccAddress exception. please,check ccAddress=" + cc);
			}
			try {
				msg.setRecipients(Message.RecipientType.CC, cc);
			} catch (MessagingException e1) {
				logger.error("sendMail add ccAddress messagingException. please,check ccAddress=" + cc);
				throw new MailException("sendMail add ccAddress messagingException. please,check ccAddress=" + cc);
			}
		}
		// 添加bcc地址
		String bcc = mail.getBcc();
		if (null != bcc && !"".equals(bcc = bcc.trim())) {
			try {
				address = InternetAddress.parse(bcc);
			} catch (AddressException e2) {
				logger.error("sendMail validate bccAddress exception. please,check bccAddress=" + bcc);
				throw new MailException("sendMail validate bccAddress exception. please,check bccAddress=" + bcc);
			}
			try {
				msg.setRecipients(Message.RecipientType.CC, cc);
			} catch (MessagingException e1) {
				logger.error("sendMail add bccAddress messagingException. please,check bccAddress=" + bcc);
				throw new MailException("sendMail add bccAddress messagingException. please,check bccAddress=" + bcc);
			}
		}
		// 添加邮件主题
		try {
			msg.setSubject(MailUtil.constructMailSubject(mail.getSubject()));
		} catch (Exception e) {
			logger.error("sendMail add subject messagingException. please,check subject=" + mail.getSubject());
			throw new MailException("sendMail add subject messagingException. please,check subject=" + mail.getSubject());
		}
		Multipart mPart = new MimeMultipart();
		// 判断邮件是否为html形式发送
		if (mail.isHtml()) {
			MimeBodyPart mBodyContent = new MimeBodyPart();
			try {
				mBodyContent.setContent(mail.getContent(), MAIL_CONTENT_HTML_TYPE);
			} catch (MessagingException e) {
				logger.error("sendMail set htmlContent messagingException:" + e.getMessage());
				throw new MailException("sendMail set htmlContent messagingException:" + e.getMessage());
			}
			try {
				mPart.addBodyPart(mBodyContent);
			} catch (MessagingException e) {
				logger.error("sendMail add htmlContent messagingException:" + e.getMessage());
				throw new MailException("sendMail add htmlContent messagingException:" + e.getMessage());
			}
			try {
				msg.setContent(mPart);
			} catch (MessagingException e) {
				logger.error("sendMail add mailContent messagingException:" + e.getMessage());
				throw new MailException("sendMail add mailContent messagingException:" + e.getMessage());
			}
		} else { // 以文本形式发送
			try {
				msg.setText(mail.getContent(), MailUtil.ENCODING);
			} catch (MessagingException e) {
				logger.error("sendMail add textContent messagingException:" + e.getMessage());
				throw new MailException("sendMail add textContent messagingException:" + e.getMessage());
			}
		}
		// 设置发送日期
		try {
			if (mail.getSendDate() != null) {
				msg.setSentDate(mail.getSendDate());
			} else {
				msg.setSentDate(new Date());
			}
		} catch (MessagingException e) {
			logger.error("sendMail set sendDate messagingException:" + e.getMessage());
			throw new MailException("sendMail set sendDate messagingException:" + e.getMessage());
		}
		// 添加附件
		List<Map<?, ?>> attachments = mail.getAttachments();
		if (null != attachments && attachments.size() > 0) {
			String fileName = null;
			String fileDesc = null;
			File file = null;
			for (Map<?, ?> attachment : attachments) {
				if ((Boolean) attachment.get("isAttachment")) {
					MimeBodyPart mBodyPart = new MimeBodyPart();
					file = (File) attachment.get("attachment");
					fileDesc = (String) attachment.get("descn");
					fileName = file.getName();
					FileDataSource fileds = new FileDataSource(file);
					try {
						mBodyPart.setDataHandler(new DataHandler(fileds));
						mBodyPart.setDescription(fileDesc);
						//处理附件名称中文问题
						try {
							fileName = MimeUtility.encodeWord(fileName, MailUtil.ENCODING,null);
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
						mBodyPart.setFileName(fileName);
						mPart.addBodyPart(mBodyPart);
					} catch (MessagingException e) {
						logger.error("sendMail add attachment fileName=" + fileName + ",messagingException:" + e.getMessage());
						throw new MailException("sendMail add attachment fileName=" + fileName + ",messagingException:" + e.getMessage());
					}
				}
			}
		}

		Transport transport = null;
		try {
				transport = session.getTransport("smtp");
		} catch (javax.mail.NoSuchProviderException e) {
			logger.error("sendMail get connect messagingException:" + e.getMessage());
			throw new MailException("sendMail get connect messagingException:" + e.getMessage());
		}
		try {
			transport.connect(MAIL_HOST, MAIL_USERNAME, MAIL_PASSWORD);
		} catch (MessagingException e) {
			logger.error("sendMail get connect messagingException:" + e.getMessage());
			throw new MailException("sendMail get connect messagingException:" + e.getMessage());
		}
		try {
			transport.sendMessage(msg, msg.getRecipients(RecipientType.TO));
			if (null != cc) {
				transport.sendMessage(msg, msg.getRecipients(RecipientType.CC));
			}
			if (null != bcc) {
				transport.sendMessage(msg, msg.getRecipients(RecipientType.BCC));
			}
		} catch (MessagingException e) {
			logger.error("sendMail send mail messagingException:" + e.getMessage());
			throw new MailException("sendMail send mail messagingException:" + e.getMessage());
		}
		logger.info("sendMail send mail success ......");
		try {
			transport.close();
		} catch (MessagingException e) {
			e.printStackTrace();
			logger.warn("sendMail colse transport  MessagingException:" + e.getMessage());
		}
	}

	public void sendHtml(String to, String subject, String content) {
		sendHtml(to, subject, content, new File[] {});
	}

	public void sendHtml(String to, String subject, String templateName, Map<?, ?> model) {
		sendHtml(to, subject, templateName, model, new File[] {});
	}
}
