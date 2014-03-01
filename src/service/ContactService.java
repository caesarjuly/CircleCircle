package service;

import java.io.InputStream;
import entity.ContactInfo;

import global.Uris;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;

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
            name = cursor.getString(cursor.getColumnIndex("display_name")); //ªÒ»°–’√˚
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
