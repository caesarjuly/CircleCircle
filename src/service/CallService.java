package service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import entity.CallInfo;
import entity.ContactInfo;
import entity.ConversationInfo;

import utils.Uris;
import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.CallLog;
import buffer.CanonicalBuffer;

public class CallService {
	private Activity activity;
	private ContactService cs;

	public CallService(Activity activity) {
		this.activity = activity;
		cs = new ContactService(activity);
	}
	
	public List<CallInfo> getCallLog(){
		Map<String, String> phoneToDate = new HashMap<String, String>();
		List<CallInfo> calls = new ArrayList<CallInfo>();
		ContentResolver resolver = activity.getContentResolver();
		Cursor cursor = resolver.query(CallLog.Calls.CONTENT_URI, new String[] {
				CallLog.Calls._ID, CallLog.Calls.NUMBER, CallLog.Calls.CACHED_NAME,
				CallLog.Calls.TYPE, CallLog.Calls.DATE }, 
				null, null,
				"date desc");
		int id = cursor.getColumnIndex(CallLog.Calls._ID);
		int phone = cursor.getColumnIndex(CallLog.Calls.NUMBER);
		int name = cursor.getColumnIndex(CallLog.Calls.CACHED_NAME);
		int type = cursor.getColumnIndex(CallLog.Calls.TYPE);
		int date = cursor.getColumnIndex(CallLog.Calls.DATE);
		
		if (cursor.getCount()>0) {
			while (cursor.moveToNext()) {
				CallInfo call = new CallInfo();
				call.setId(cursor.getInt(id));
				call.setDate(cursor.getLong(date));
				call.setPhone(cursor.getString(phone));
				if(phoneToDate.containsKey(cursor.getString(phone))){
					continue;
				}else{
					//call.setName(cs.getContactNameByPhone(cursor.getString(phone)));
					call.setType(cursor.getInt(type));
					//call.setPhoto(cs.getConvPhotoByPhone(cursor.getString(phone)));
					calls.add(call);
					phoneToDate.put(cursor.getString(phone), cursor.getString(date));
				}
			}
			cursor.close();
		}
		return calls;

	}

	public int deleteCall(String phone) {
		// TODO Auto-generated method stub
		ContentResolver resolver = activity.getContentResolver();
		int result = resolver.delete(CallLog.Calls.CONTENT_URI, "number=?", new String[] { phone });
		return result;
	}
}
