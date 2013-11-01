package cn.slimsmart.mailer.template;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.slimsmart.mailer.exception.MailException;
import cn.slimsmart.mailer.util.ResourceUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * 邮件内容使用模板，内容加载
 * @author zhutianwei
 * @date 2011-05-29
 * @version v1.0
 */
public class MailTemplateGenerator {

	private static final Logger logger = LoggerFactory.getLogger(MailTemplateGenerator.class);

	private static final String TPL_PATH = ResourceUtil
			.getResources("mail.template.path");

	private static Configuration config = null;
	
	/**
	 * 单例获取config
	 * @return
	 */
	private static Configuration getConfiguration(){
		if(null == config){
			config = new Configuration();
			config.setClassForTemplateLoading(MailTemplateGenerator.class,
					TPL_PATH);
		}
		return config;
	}

	/**
	 * 获取邮件模板内容
	 * @param templateFileName  模板名称
	 * @param model  模板数据
	 * @return
	 * @throws MailException
	 */
	public static String generateEmailContent(String templateFileName,
			Map<?, ?> model) throws MailException {
		try {
			Template tmpl = getConfiguration().getTemplate(templateFileName);
			StringWriter result = new StringWriter();
			tmpl.process(model, result);
			return result.toString();
		} catch (IOException e) {
			logger.error("loading templateFile Exception:" + e.getMessage());
			throw new MailException("loading templateFile Exception:"
					+ e.getMessage());
		} catch (TemplateException e) {
			logger.error("parse templateContent Exception:" + e.getMessage());
			throw new MailException("parse templateContent Exception:"
					+ e.getMessage());
		}

	}
}
