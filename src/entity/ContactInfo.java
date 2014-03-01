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
