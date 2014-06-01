package service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



import utils.CharacterParser;
import utils.ContextUtil;
import utils.Uris;
import buffer.ContactBuffer;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.GroupMembership;
import android.provider.ContactsContract.CommonDataKinds.Im;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.util.Log;
import entity.ContactInfo;


public class ContactService{
	private ContextUtil activity;
	private Uri uri;
	public static List<ContactInfo> contactinfos;
	private List<ContactInfo> contactinfos2;
	static  HashMap<Integer, String> contactBuffer = new HashMap<Integer, String>();
	private CharacterParser characterParser;
	
	public ContactService() {
		contactinfos = new ArrayList<ContactInfo>();
		contactinfos2 = new ArrayList<ContactInfo>();
		this.activity = ContextUtil.getInstance();
		this.uri = Uri.parse(Uris.Contacts_URI_RAW);
	}	
	
	public List<ContactInfo> getContactInfo() {
		ContentResolver resolver = activity.getContentResolver();
		characterParser = CharacterParser.getInstance();
		Cursor cursor = resolver.query(Uri.parse(Uris.Contacts_URI_RAW),
				null, RawContacts.DELETED+"=0", null, null);
		int id = cursor.getColumnIndex("_id");//获取id
		int name = cursor.getColumnIndex("display_name"); //获取姓名
		
		if (cursor != null) {
			while (cursor.moveToNext()) {
				ContactInfo contactinfo = new ContactInfo();
				Cursor groupcursor=null;
		        String groupid="";
		        String[] groups= new String[]{GroupMembership.GROUP_ROW_ID};
		        //int value=171;
		        if(contactBuffer.containsKey(cursor.getInt(id))){
		        	contactinfo.setGroupid(contactBuffer.get(cursor.getInt(id)));
		        }else{
		        	 String where=GroupMembership.RAW_CONTACT_ID+" = ?"+" AND " +Data.MIMETYPE + "='" + GroupMembership.CONTENT_ITEM_TYPE+"'";
				        groupcursor=resolver.query(Data.CONTENT_URI, groups, where, new String[]{Integer.toString(cursor.getInt(id))}, null);
				        //设置groupid
				        if(groupcursor!=null&&groupcursor.getCount()!=0)
				        {
				        	//Log.i("123", "groupcursor："+groupcursor.getCount());
				        	groupcursor.moveToNext();
				        	groupid=groupcursor.getString(groupcursor.getColumnIndex(GroupMembership.GROUP_ROW_ID));
				          	//  Log.i(Integer.toString(cursor.getInt(id)), "查询分组结果："+groupid);
				          	contactinfo.setGroupid(groupid);
				        }
				        else
				        {
				        	contactinfo.setGroupid("999");
				        	//Log.i(Integer.toString(cursor.getInt(id)), "查询分组结果："+groupid);
				        }
				        groupcursor.close();
				        contactBuffer.put(cursor.getInt(id), contactinfo.getGroupid());
		        }
		       
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
	
	public List<ContactInfo> getContactInfoWithoutGroup() {
		ContentResolver resolver = activity.getContentResolver();
		characterParser = CharacterParser.getInstance();
		Cursor cursor = resolver.query(Uri.parse(Uris.Contacts_URI_RAW),
				null, RawContacts.DELETED+"=0", null, null);
		int id = cursor.getColumnIndex("_id");//获取id
		int name = cursor.getColumnIndex("display_name"); //获取姓名
		
		if (cursor != null) {
			while (cursor.moveToNext()) {
				ContactInfo contactinfo = new ContactInfo();
		        //int value=171;
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
	
	
	
	private void getGroup(ContentResolver resolver,String contactId){
		 Cursor cursor=null;
		   
		 String[] groups= new String[]{GroupMembership.GROUP_ROW_ID};
		 String where=GroupMembership.CONTACT_ID+"="+contactId+" AND" +Data.MIMETYPE + "=" + " ' " + GroupMembership.CONTENT_ITEM_TYPE+" ' ";
		 cursor=resolver.query(Data.CONTENT_URI, groups, where, null, null);
		   
		 while(cursor.moveToNext()){
		  String id=cursor.getString(cursor.getColumnIndex(GroupMembership.GROUP_ROW_ID));
		  Log.i("联系人分组", "查询分组结果："+id);
		 }
		 cursor.close();
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
                 ContactsContract.CommonDataKinds.Phone.RAW_CONTACT_ID + "=" + contact_id, 
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
	
	
	public Bitmap getConvPhotoByPhone(String phone){
		Bitmap photo = null;
		ContentResolver resolver = activity.getContentResolver();
		Uri uri = Uri.parse(Uris.Contacts_URI_PHONE + phone);  
        Cursor cursor = resolver.query(uri, null, null,  
                        null, null);  
        if(cursor.getCount()>0) {
        	cursor.moveToFirst();
        	Long contactID = cursor.getLong(cursor.getColumnIndex("contact_id")); 
            uri = ContentUris.withAppendedId(  
                    ContactsContract.Contacts.CONTENT_URI, contactID);  
            InputStream input = ContactsContract.Contacts  
                    .openContactPhotoInputStream(resolver, uri);  
            photo = BitmapFactory.decodeStream(input);
            }
        cursor.close();
        return photo;
	}
	
	public String getContactNameByPhone(String phone){
		String name = null;
		ContentResolver resolver = activity.getContentResolver();
		Uri uri = Uri.parse(Uris.Contacts_URI_PHONE + phone);  
        Cursor cursor = resolver.query(uri, new String[]{"display_name"}, null,  
                        null, null);  
        if(cursor.getCount()>0) {
        	cursor.moveToFirst();
            name = cursor.getString(cursor.getColumnIndex("display_name")); //获取姓名
            }
        
        cursor.close();
        return name;
	}
	
	
}


