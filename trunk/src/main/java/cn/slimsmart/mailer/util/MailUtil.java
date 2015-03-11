package cn.slimsmart.mailer.util;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.internet.MimeUtility;

import com.sun.mail.util.BASE64EncoderStream;

/**
 * 邮件公共方法类
 * 
 * @author zhutianwei
 * @date 2011-05-29
 * @version v1.0
 */
public class MailUtil {

	public static final String MAIL_ADDRESS_REGEX = "^([0-9a-zA-Z]([-\\._]?[0-9a-zA-Z])*?@([0-9a-zA-Z](-?[0-9a-zA-Z])*\\.)*[a-zA-Z]{2,9}?)$";

	public static final String ENCODING = "GBK";
	 
	/**
	 * 检查邮件地址是否合法
	 * 
	 * @param mail
	 * @return true:邮件地址合法   false:邮件地址不合法
	 */
	public static boolean isMailAddress(String mail) {
		Pattern pattern = null;
		Matcher matcher = null;
		int atpos = 0;
		atpos = mail.indexOf('@');
		String mailBox = mail.substring(0, atpos);
		String host = mail.substring(atpos + 1);
		if (mailBox.length() == 0 || host.length() == 0) {
			return false;
		}
		pattern = Pattern.compile(MAIL_ADDRESS_REGEX, Pattern.CASE_INSENSITIVE);
		matcher = pattern.matcher(mail);
		if (!matcher.matches()){
			return false;
		}
		return true;
	}
	
	/**
	 * 转换发件人的邮件地址名称
	 * @param show  邮件中显示发件人名称
	 * @param emailFrom  发件人的邮件地址
	 * @return 转换后的邮件from地址
	 */
	 public static String constructMailFrom(String show, String emailFrom)
	  {
	    if (show == null){
	      return emailFrom;
	    }
	     try {
			show = new String(BASE64EncoderStream.encode(show.getBytes(ENCODING)));
		} catch (UnsupportedEncodingException e) {
			  return emailFrom;
		}
	    show = "\"=?" + ENCODING + "?B?" + show + "?=";
	    show = show + "\"<" + emailFrom + ">";
	    return show;
	  }
	
	 /**
	  * 处理邮件标题乱码
	  * @param subject  邮件标题
	  * @return 转换后的邮件标题
	  */
	 public static String constructMailSubject(String subject){
		try {
			return MimeUtility.encodeText(subject,ENCODING,"B");
		} catch (UnsupportedEncodingException e) {
			return subject;
		}
	}
	 
	 public static void main(String[] aa){
		System.out.println( constructMailFrom("发件人名称","xxx@163.com"));
	 }
}
