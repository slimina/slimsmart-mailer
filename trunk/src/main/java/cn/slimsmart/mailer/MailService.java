package cn.slimsmart.mailer;

import java.io.File;
import java.util.Map;

import cn.slimsmart.mailer.entity.Mail;

/**
 * 发送邮件接口
 * @author zhutianwei
 * @date 2011-05-29
 * @version v1.0
 */
public interface MailService {

	/**
	 * 发送文本邮件
	 * @param to   发件人
	 * @param subject 邮件主题
	 * @param content 邮件内容
	 */
	void sendText(String to, String subject, String content);

	/**
	 * 发送文本模板邮件
	 * @param to   发件人
	 * @param subject 邮件主题
	 * @param templateName 邮件模板名称
	 * @param model 模板数据
	 */
	void sendText(String to, String subject, String templateName,
			Map<?, ?> model);

	/**
	 * 发送HTML邮件
	 * @param to   发件人
	 * @param subject 邮件主题
	 * @param content 邮件内容
	 */
	void sendHtml(String to, String subject, String content);

	/**
	 * 发送HTML模板邮件
	 * @param to   发件人
	 * @param subject 邮件主题
	 * @param templateName 邮件模板名称
	 * @param model 模板数据
	 */
	void sendHtml(String to, String subject, String templateName,
			Map<?, ?> model);

	/**
	 * 发送HTML邮件,带附件
	 * @param to   发件人
	 * @param subject 邮件主题
	 * @param content 邮件内容
	 * @param attachementFile 附件
	 */
	void sendHtml(String to, String subject, String content,
			File... attachementFile);

	/**
	 * 发送HTML模板邮件,带附件
	 * @param to   发件人
	 * @param subject 邮件主题
	 * @param templateName 邮件模板名称
	 * @param model 模板数据
	 * @param attachementFile 附件
	 */
	void sendHtml(String to, String subject, String templateName,
			Map<?, ?> model, File... attachementFile);

	/**
	 * 发送邮件
	 * @param mail  邮件实体
	 */
	void sendMail(Mail mail);
}
