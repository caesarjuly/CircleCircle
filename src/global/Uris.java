package global;


/**
 * class name：Uri
 * class description：定义公用的Uri资源
 * PS： 
 * Date:2014-2-21
 * 
 * @version 1.00
 * @author YF
 */
public class Uris {
	/**
	 * 所有的短信
	 */
	public static final String SMS_URI_ALL = "content://sms/";
	/**
	 * 收件箱短信
	 */
	public static final String SMS_URI_INBOX = "content://sms/inbox";
	/**
	 * 发件箱短信
	 */
	public static final String SMS_URI_SEND = "content://sms/sent";
	/**
	 * 草稿箱短信
	 */
	public static final String SMS_URI_DRAFT = "content://sms/draft";
	/**
	 * 所有联系人
	 */
	public static final String Contacts_URI_ALL = "content://com.android.contacts/contacts"; 
	/**
	 * 单个联系人
	 * #代表联系人id
	 */
	
	public static final String Phone_CONTENT_URI = "content:// com.android.contacts/data/phones"; 
	/**
	 * 讀取聯繫人信息和號碼
	 */
	public static final String Contacts_URI_SINGLE = "content://com.android.contacts/contacts/#/data"; 
}
