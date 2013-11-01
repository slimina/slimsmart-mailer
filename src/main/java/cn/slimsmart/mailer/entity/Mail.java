package cn.slimsmart.mailer.entity;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 发送邮件实体类
 * @author zhutianwei
 * @date 2011-05-29
 * @version v1.0
 */
public class Mail implements Serializable {
	
	private static final long serialVersionUID = -5497842145903649700L;
	
	private String subject;
	private String content;
	private boolean debug = false;
	private String from;
	
	/**  多地址用半角逗号分隔  */
	private String to;
	/**  多地址用半角逗号分隔  */
	private String cc;
	/**  多地址用半角逗号分隔  */
	private String bcc;
	private Date sendDate;
	
	private boolean isHtml = false;

	//发送HTML邮件支持
	private List<Map<?,?>> attachments = new ArrayList<Map<?,?>>();

	public Mail(String to ,String subject, String content) {
		this.to = to;
		this.subject = subject;
		this.content = content;
	}

	public Mail() {
	}

	public String getSubject() {
		return this.subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public boolean isHtml() {
		return this.isHtml;
	}

	public void setHtml(boolean isHtml) {
		this.isHtml = isHtml;
	}

	public List<Map<?,?>> getAttachments() {
		return Collections.unmodifiableList(attachments);
	}

	public void setAttachments(List<Map<?,?>> attachments) {
		this.attachments = attachments;
	}
	
	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}
	
	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getCc() {
		return cc;
	}

	public void setCc(String cc) {
		this.cc = cc;
	}

	public String getBcc() {
		return bcc;
	}

	public Date getSendDate() {
		return sendDate;
	}

	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}

	public void setBcc(String bcc) {
		this.bcc = bcc;
	}
    
	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	/**
	 * 添加文件
	 * @param descn  附件描述
	 * @param file    附件
	 * @param isAttachment  是否是附件    true：附件  false：未实现，邮件HTML中图片(一般不使用)
	 */
	public void addAttachment(String descn, File file, boolean isAttachment) {
		Map<String, Object> attachment = new HashMap<String, Object>();
		attachment.put("descn", descn);
		attachment.put("attachment", file);
		attachment.put("isAttachment", Boolean.valueOf(isAttachment));
		this.attachments.add(attachment);
	}
}
