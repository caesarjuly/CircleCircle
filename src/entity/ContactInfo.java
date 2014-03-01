package entity;

import android.graphics.Bitmap;

public class ContactInfo {
	/**
	 * 联系人的姓名
	 */
	private String name;
	/**
	 * 电话
	 */
	private String phone;
	
	public String getSortKey() {
		return sortKey;
	}

	public void setSortKey(String sortKey) {
		this.sortKey = sortKey;
	}

	private String sortKey;
	/**
	 * 头像
	 */
	private Bitmap photo;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Bitmap getPhoto() {
		return photo;
	}

	public void setPhoto(Bitmap photo) {
		this.photo = photo;
	}
	
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

}
