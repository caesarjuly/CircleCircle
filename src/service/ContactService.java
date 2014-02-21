package service;

import global.Uris;
import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract.PhoneLookup;

public class ContactService {
	String contactName;
	private Activity activity;
	private Uri uri;
	
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
            String name = cursor.getString(cursor.getColumnIndex(PhoneLookup.DISPLAY_NAME)); //ªÒ»°–’√˚
            return name;
        }else{
        	return null;
        }
       
	}
}
