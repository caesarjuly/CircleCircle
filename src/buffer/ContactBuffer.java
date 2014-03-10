package buffer;

import entity.ContactInfo;
import global.Uris;

import java.util.HashMap;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

public class ContactBuffer {
	private HashMap<String, String> cd;
	private HashMap<String, ContactInfo> cb;
	
	private Activity activity;
	
	public ContactBuffer() {
		cb = new HashMap<String, ContactInfo>();
	}


	public ContactBuffer(Activity activity) {
		//构造一个哈希映射，对应_id和displayname
		cd = new HashMap<String, String>();
		this.activity = activity;
		initBuffer();
	}
	
	private void initBuffer() {
		ContentResolver resolver = activity.getContentResolver();
		Cursor cursor = resolver.query(
				Uri.parse(Uris.Contacts_URI_RAW), null, null, null,
				null);
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				String id = cursor.getString(cursor.getColumnIndex("contact_id")); 
				String display_name = cursor.getString(cursor.getColumnIndex("display_name"));
				cd.put(id, display_name);
				}
		}
		cursor.close();
	}
	
	//获取联系人buffer
	public HashMap<String, String> getcontactBuffer(){
		return cd;
	}
	
	public HashMap<String, ContactInfo> getBuffer(){
		return cb;
	}
}
