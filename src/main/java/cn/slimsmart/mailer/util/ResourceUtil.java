package cn.slimsmart.mailer.util;

import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 资源加载
 * @author zhutianwei
 * @date 2011-5-29
 * @version V1.0
 */
public class ResourceUtil {

	private static final Logger logger = LoggerFactory.getLogger(ResourceUtil.class);
	
	public static String getResources(String prop) {
		ResourceBundle rb = ResourceBundle.getBundle("mail");
		String result = rb.getString(prop);
		logger.debug("propertiy " + prop + " :[" + result + "]");
		return result;
	}
}
