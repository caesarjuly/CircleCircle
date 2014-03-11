package service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



import utils.CharacterParser;
import utils.Uris;
import buffer.ContactBuffer;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;
import entity.ContactInfo;


public class ContactService{
	private Activity activity;
	private Uri uri;
	private List<ContactInfo> contactinfos;
	private HashMap<String, String> contactBuffer;
	private CharacterParser characterParser;
	
	public ContactService(Activity activity) {
		contactinfos = new ArrayList<ContactInfo>();
		this.activity = activity;
		this.uri = Uri.parse(Uris.Contacts_URI_RAW);
		ContactBuffer cb = new ContactBuffer(activity);
		contactBuffer = cb.getcontactBuffer();   //获取联系人buffer
	}	
	
	public List<ContactInfo> getContactInfo() {
		ContentResolver resolver = activity.getContentResolver();
		characterParser = CharacterParser.getInstance();
		Cursor cursor = resolver.query(Uri.parse(Uris.Contacts_URI_RAW),
				null, null, null, null);
		int id = cursor.getColumnIndex("contact_id");
		int name = cursor.getColumnIndex("display_name"); //获取姓名
		
		if (cursor != null) {
			while (cursor.moveToNext()) {
				ContactInfo contactinfo = new ContactInfo();
				contactinfo.setId(cursor.getInt(id));
				contactinfo.setName(cursor.getString(name));
				String named=cursor.getString(name);
				//if(Integer.parseInt(named)==1);
				if(named!=null)
				
				{String pinyin = characterParser.getSelling(named);
					String sortString = pinyin.substring(0, 1).toUpperCase();
					
					// 正则表达式，判断首字母是否是英文字母
					if(sortString.matches("[A-Z]")){
						contactinfo.setSortLetters(sortString.toUpperCase());
					}else{
						contactinfo.setSortLetters("#");
					}
					
					contactinfos.add(contactinfo);
				}
			}
			cursor.close();
		}
		return contactinfos;
	}
	
	/**
	 *通过Id获取联系人电话号码 
	 *Date:2014-3-5
	 *Author：王东东
	 */
	public ContactInfo getContactById(int id,String cname){
		ContactInfo ct = new ContactInfo();
		String username="";
		String TAG="abc";
		String phoneNumber="";
		username=cname;
		int contact_id;
		contact_id=id;
		ContentResolver resolver = activity.getContentResolver();

		 Cursor phones = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, 
                 null, 
                 ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contact_id, 
                 null, null);
         int phoneIndex = 0;
         if(phones.getCount() > 0) {
             phoneIndex = phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
         }
         while(phones.moveToNext()) {
             phoneNumber = phones.getString(phoneIndex);
             System.out.println("number:"+phoneNumber);
             Log.i(TAG,phoneNumber);    
         }

         
			uri = ContentUris.withAppendedId(  
					ContactsContract.Contacts.CONTENT_URI, contact_id); 
			InputStream input = ContactsContract.Contacts  
					.openContactPhotoInputStream(resolver, uri);  
			Bitmap photo = BitmapFactory.decodeStream(input);
			
			String abc=phoneNumber;
			ct.setPhoto(photo);
			ct.setName(username);
			ct.setPhone(phoneNumber);
			phones.close();
			return ct;
		
		
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
	
	
}


