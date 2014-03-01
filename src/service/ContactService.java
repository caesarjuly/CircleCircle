package service;


import java.util.ArrayList;
import java.util.List;

import entity.ContactInfo;
import entity.SmsInfo;

import java.io.InputStream;

import global.Uris;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;
import android.util.Log;


public class ContactService {
	private Activity activity;
	
	public ContactService(Activity activity){
		this.activity = activity;
	}
	
	public ContactInfo getContactByPhone(String phone){
		ContactInfo ct = new ContactInfo();
		String name;
		ContentResolver resolver = activity.getContentResolver();
		Uri uri = Uri.parse(Uris.Contacts_URI_PHONE + phone);  
        Cursor cursor = resolver.query(uri, null, null,  
                        null, null);  
        if(cursor.getCount()>0) {
        	cursor.moveToFirst();
        	Long contactID = cursor.getLong(cursor.getColumnIndex("contact_id")); 
            name = cursor.getString(cursor.getColumnIndex("display_name")); //获取姓名
            uri = ContentUris.withAppendedId(  
                    ContactsContract.Contacts.CONTENT_URI, contactID);  
            InputStream input = ContactsContract.Contacts  
                    .openContactPhotoInputStream(resolver, uri);  
            Bitmap photo = BitmapFactory.decodeStream(input);
            ct.setName(name);
            ct.setPhoto(photo);
            }
        ct.setPhone(phone);
        cursor.close();
        return ct;
	}
	
	/**
	 * 获取联系人的姓名和号码
	 * Date:2014-2-23
	 * 
	 */
	public List<ContactInfo> getContactInfo() {
		String strPhoneNumber="";
		List<ContactInfo> contact_infos=new ArrayList<ContactInfo>();
		ContentResolver resolver = activity.getContentResolver();

		Cursor cursor = resolver.query(Uri.parse(Uris.Contacts_URI_ALL), null,null,null, null);
		
		int contact_name = cursor.getColumnIndex("DISPLAY_NAME");
		
		int contact_num = cursor.getColumnIndex("NUMBER");
		
		if(cursor.getCount()>0){ 
			if (cursor != null) {
				while (cursor.moveToNext()) {
					
					ContactInfo contactinfo = new ContactInfo();
					contactinfo.setName(cursor.getString(contact_name));
					
					//得到电话号码 
					// 获取联系人的ID号，在SQLite中的数据库ID  
					String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

					        Cursor phone = resolver.query(  

					        ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,  

					        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = "  

					        + contactId, null, null);  

					while (phone.moveToNext()) {  

					strPhoneNumber = phone.getString( 

					                 phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)); // 手机号码字段联系人可能不止一个  	  

					   }  
					//设置号码	
					contactinfo.setPhone(strPhoneNumber);

					contact_infos.add(contactinfo);
					
				}
				cursor.close();
			}
		return contact_infos;
	}
		 else 
			 {
				 Log.d("abc","kongzhizhen");
				 return null;}
			}
}
