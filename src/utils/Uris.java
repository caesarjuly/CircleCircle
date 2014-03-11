package utils;


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
	 * 短信会话
	 */
	public static final String CONVERSATION_URI = "content://mms-sms/conversations/";
	/**
	 * 简单短信会话
	 */
	public static final String CONVERSATION_URI_ALL = "content://mms-sms/conversations?simple=true";
	/**
	 * 获得所有会话联系人
	 */
	public static final String CANONICAL_URI_ADDRESSES = "content://mms-sms/canonical-addresses";
	/**
	 * 根据id获取联系人
	 */
	public static final String CANONICAL_URI_ADDRESS = "content://mms-sms/canonical-address/";
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

	
	//对raw_contacts表添加、删除、更新操作：
	public static final String Contacts_URI_RAW= "content://com.android.contacts/raw_contacts";
	
	//对data表添加、删除、更新操作：
	public static final String Contacts_URI_DATA = "content://com.android.contacts/data";
	
	//根据email对data表查询
	public static final String Contacts_URI_EMAIL = "content://com.android.contacts/data/emails/filter/";

	//根据电话号码对data表查询
	public static final String Contacts_URI_PHONE = "content://com.android.contacts/data/phones/filter/";


}
