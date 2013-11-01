package cn.slimsmart.mailer.exception;

/**
 * 发送邮件异常
 * @author zhutianwei
 * @date 2011-05-29
 * @version v1.0
 */
public class MailException extends RuntimeException
{
	  private static final long serialVersionUID = -7200901958402480252L;

	  public MailException()
	  {
	  }

	  public MailException(String message)
	  {
	    super(message);
	  }

	  public MailException(String message, Throwable cause)
	  {
	    super(message, cause);
	  }

	  public MailException(Throwable cause)
	  {
	    super(cause);
	  }

}
