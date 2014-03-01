package entity;

import android.graphics.Bitmap;

public class ThreadInfo {
	/**
	 * 会话id
	 */
	private int id;
	/**
	 * 最后一条短信日期
	 */
	private long date;
	/**
	 * 会话短信数
	 */
	private int messageCount;
	/**
	 * 会话方id，指向canonical表
	 */
	private String recipientId;
	/**
	 * 最后一条短信
	 */
	private String snippet;
	/**
	 * 短信字符集
	 */
	private int snippetCs;
	/**
	 * 是否已读，已读为1，未读为0
	 */
	private int read;
	/**
	 * 会话类型,Type of the thread, either Threads.COMMON_THREAD or Threads.BROADCAST_THREAD
	 */
	private int type;
	/**
	 * 是否错误，有错误为1，没有为0
	 */
	private int error;
	/**
	 * 是否有附件，没有为1，有为0
	 */
	private int hasAttachment;
	/**
	 * 是否为群发，是为1，否为0
	 */
	private int isMass;
	/**
	 * 持有联系人对象
	 */
	private ContactInfo cti;
	/**
	 * 持有群发联系人对象集
	 */
	private ContactInfo[] ctis;
	
	public ThreadInfo(){
		isMass = 0;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public long getDate() {
		return date;
	}
	public void setDate(long date) {
		this.date = date;
	}
	public int getMessageCount() {
		return messageCount;
	}
	public void setMessageCount(int messageCount) {
		this.messageCount = messageCount;
	}
	public String getRecipientId() {
		return recipientId;
	}
	public void setRecipientId(String recipientId) {
		this.recipientId = recipientId;
	}
	public String getSnippet() {
		return snippet;
	}
	public void setSnippet(String snippet) {
		this.snippet = snippet;
	}
	public int getSnippetCs() {
		return snippetCs;
	}
	public void setSnippetCs(int snippetCs) {
		this.snippetCs = snippetCs;
	}
	public int getRead() {
		return read;
	}
	public void setRead(int read) {
		this.read = read;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getError() {
		return error;
	}
	public void setError(int error) {
		this.error = error;
	}
	public int getHasAttachment() {
		return hasAttachment;
	}
	public void setHasAttachment(int hasAttachment) {
		this.hasAttachment = hasAttachment;
	}
	public ContactInfo getCti() {
		return cti;
	}

	public void setCti(ContactInfo cti) {
		this.cti = cti;
	}

	public ContactInfo[] getCtis() {
		return ctis;
	}

	public void setCtis(ContactInfo[] ctis) {
		this.ctis = ctis;
	}
	public int getIsMass() {
		return isMass;
	}

	public void setIsMass(int isMass) {
		this.isMass = isMass;
	}
	

}
