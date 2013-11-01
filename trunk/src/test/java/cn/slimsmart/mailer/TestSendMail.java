package cn.slimsmart.mailer;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;
import cn.slimsmart.mailer.impl.MailServiceImpl;
import cn.slimsmart.mailer.util.MailUtil;

/**
 * 发送邮件测试
 * 
 * @author zhutianwei
 * @date 2011-05-29
 * @version v1.0
 */
public class TestSendMail extends TestCase {

	MailService mailService = null;

	@Override
	protected void setUp() throws Exception {
		mailService = new MailServiceImpl();
		super.setUp();
	}

	public void testSendText() {
		System.out.println(MailUtil.constructMailFrom("Java开发者", "java_2010@163.com"));;
		mailService.sendText("tianwei7518@163.com", "测试", "邮件发送测试");
	}

	public void testSendTextByTemplate() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("name", "朱天伟");
		map.put("date", "2011-05-22");
		mailService.sendText("java_2010@163.com", "你好", "test.ftl", map);
	}

	public void testSendHtml() {
		mailService.sendHtml("java_2010@163.com", "测试HTML",
				"<div>fdsfsfsda<h2>fdsfsafd</h2></div>");
	}

	public void testSendHtmlByTemplate() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("name", "朱天伟");
		map.put("date", "2011-05-22");
		mailService.sendHtml("java_2010@163.com", "你好", "test.ftl", map);
	}

	public void testSendHtmlAndAttachmentsByTemplate() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("name", "朱天伟");
		map.put("date", "2011-05-22");
		mailService.sendHtml("java_2010@163.com", "你好", "test.ftl", map,
				new File[] { new File("stat.properties"),
						new File("server_bak.xml") });
	}

}
