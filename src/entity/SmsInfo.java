package entity;

import android.graphics.Bitmap;

/**
 * class name：SmsInfo
 * class description：短信信息实体类
 * PS： 
 * Date:2014-2-21
 * 
 * @version 1.00
 * @author YF
 */

/*	 _id：短信序号，如100 　　
* 　　thread_id：对话的序号，如100，与同一个手机号互发的短信，其序号是相同的 　　
* 　　address：发件人地址，即手机号，如+8613811810000 　　
* 　　person：发件人，如果发件人在通讯录中则为具体姓名，陌生人为null 　　
* 　　date：日期，long型，如1256539465022，可以对日期显示格式进行设置 　　
* 　　protocol：协议0SMS_RPOTO短信，1MMS_PROTO彩信 　　 　　
* 	 read：是否阅读0未读，1已读 　　
* 　　status：短信状态-1接收，0complete,64pending,128failed 　　
* 　　type：短信类型1是接收到的，2是已发出 　　 　　body：短信具体内容 　　
* 　　service_center：短信服务中心号码编号，如+8613800755500
*/
public class SmsInfo {
	/**
	 * id
	 */
	private long id;
	/**
	 * 会话id
	 */
	private long thread_id;
	/**
	 * 电话
	 */
	private String address;
	/**
	 * 姓名
	 */
	private String person;
	/**
	 * 短信内容
	 */
	private String protocol;
	/**
	 * 短信内容
	 */
	private String service_center;
	/**
	 * 发送短信的日期和时间
	 */
	private long date;
	/**
	 * 短信类型1是接收到的，2是已发出
	 */
	private int type;
	/**
	 * 是否阅读
	 */
	private int read;
	/**
	 * 状态
	 */
	private int status;
	/**
	 * 短信服务中心
	 */
	private String body;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getThread_id() {
		return thread_id;
	}
	public void setThread_id(long thread_id) {
		this.thread_id = thread_id;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPerson() {
		return person;
	}
	public void setPerson(String person) {
		this.person = person;
	}
	public String getProtocol() {
		return protocol;
	}
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	public String getService_center() {
		return service_center;
	}
	public void setService_center(String service_center) {
		this.service_center = service_center;
	}
	public long getDate() {
		return date;
	}
	public void setDate(long date) {
		this.date = date;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getRead() {
		return read;
	}
	public void setRead(int read) {
		this.read = read;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}

	
}