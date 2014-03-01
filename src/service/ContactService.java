package service;

import java.util.ArrayList;
import java.util.List;

import entity.Contactinfo;
import entity.SmsInfo;
import global.Uris;
import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;
import android.util.Log;

public class ContactService {
	String contactName;
	public String adb="123";
	private Activity activity;
	private Uri uri;
	private String[] projection = new String[] { "_id", "num", "person" };
	
	public ContactService(Activity activity, Uri uri){
		this.activity = activity;
		this.uri = uri;
	}
	
	public String getNameByPhone(String phone){
		ContentResolver resolver = activity.getContentResolver();
		Cursor cursor = resolver.query(Uri.withAppendedPath(
                PhoneLookup.CONTENT_FILTER_URI, phone), new String[] {
                PhoneLookup._ID,
                PhoneLookup.NUMBER,
                PhoneLookup.DISPLAY_NAME,
                PhoneLookup.TYPE, PhoneLookup.LABEL }, null, null,   null );
        if(cursor.getCount()>0) {
        	cursor.moveToFirst();
            String name = cursor.getString(cursor.getColumnIndex(PhoneLookup.DISPLAY_NAME)); //获取姓名
            return name;
        }else{
        	return null;
        }
       
	}
	
	
	/**
	 * 获取联系人的姓名和号码
	 * Date:2014-2-23
	 * 
	 */
	public List<Contactinfo> getContactInfo() {
		String strPhoneNumber="";
		List<Contactinfo> contact_infos=new ArrayList<Contactinfo>();
		ContentResolver resolver = activity.getContentResolver();

		Cursor cursor = resolver.query(uri, null,null,null, null);
		
		int contact_name = cursor.getColumnIndex("DISPLAY_NAME");
		
		int contact_num = cursor.getColumnIndex("NUMBER");
		
		if(cursor.getCount()>0){ 
			if (cursor != null) {
				while (cursor.moveToNext()) {
					
					Contactinfo contactinfo = new Contactinfo();
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
					contactinfo.setPhoneNumber(strPhoneNumber);

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
