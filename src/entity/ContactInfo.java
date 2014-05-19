package entity;

import java.io.Serializable;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class ContactInfo implements Parcelable {
	/**
	 * 联系人的姓名
	 */
	private String name;
	/**
	 * 电话
	 */
	private String phone;
	/**
	 * id
	 */
	private int id;

	private ContactInfo cti;

	private String sortLetters; // 显示数据拼音的首字母

	private String sortKey;
	/**
	 * 头像
	 */
	private Bitmap photo;

	public ContactInfo getCti() {
		return cti;
	}

	public void setCti(ContactInfo cti) {
		this.cti = cti;
	}

	public String getSortKey() {
		return sortKey;
	}

	public void setSortKey(String sortKey) {
		this.sortKey = sortKey;
	}

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

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public String getSortLetters() {
		return sortLetters;
	}

	public void setSortLetters(String sortLetters) {
		this.sortLetters = sortLetters;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel arg0, int arg1) {
		// TODO Auto-generated method stub
		arg0.writeInt(id);
		arg0.writeString(name);
		arg0.writeString(phone);

	}

	public static final Parcelable.Creator<ContactInfo> CREATOR = new Creator<ContactInfo>() {
		@Override
		public ContactInfo createFromParcel(Parcel source) {
			ContactInfo ci = new ContactInfo();
			ci.id = source.readInt();
			ci.name = source.readString();
			ci.phone = source.readString();
			return ci;
		}

		@Override
		public ContactInfo[] newArray(int size) {
			return new ContactInfo[size];
		}
	};

}
