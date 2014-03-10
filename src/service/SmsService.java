package service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import buffer.CanonicalBuffer;

import entity.ContactInfo;
import entity.ConversationInfo;
import global.Uris;
import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

/**
 * class name：SmsService class description：获取手机中的各种短信信息 PS： 需要权限
 * <uses-permission android:name="android.permission.READ_SMS" /> Date:2014-2-21<BR>
 * 
 * @version 1.00
 * @author YF
 */
public class SmsService {
	private Activity activity;
	private Uri uri;
	private List<ConversationInfo> threads;
	private HashMap<String, String> canonicalBuffer;

	public SmsService(Activity activity) {
		threads = new ArrayList<ConversationInfo>();
		this.activity = activity;
		this.uri = Uri.parse(Uris.SMS_URI_ALL);
		CanonicalBuffer cb = new CanonicalBuffer(activity);
		canonicalBuffer = cb.getBuffer();
	}

	/**
	 * Role:获取短信的各种信息 Date:2014-2-21
	 * 
	 */
	public List<ConversationInfo> getSmsInfo() {
		ContentResolver resolver = activity.getContentResolver();
		// 使用hack完成distinct查询，也可自己覆写provider实现
		// Cursor cursor = resolver.query(uri, new String[] {
		// "DISTINCT thread_id", "COUNT(thread_id) AS dialogNum",
		// "address", "body", "person", "date", "type", "read" }, // DISTINCT
		// "address IS NOT NULL) GROUP BY (thread_id", // GROUP BY
		// null, null);
		Cursor cursor = resolver.query(Uri.parse(Uris.CONVERSATION_URI_ALL),
				null, null, null, "date DESC");
		int id = cursor.getColumnIndex("_id");
		int date = cursor.getColumnIndex("date");
		int error = cursor.getColumnIndex("error");
		int hasAttachment = cursor.getColumnIndex("has_attachment");
		int messageCount = cursor.getColumnIndex("message_count");
		int read = cursor.getColumnIndex("read");
		int recipientId = cursor.getColumnIndex("recipient_ids");
		int snippet = cursor.getColumnIndex("snippet");
		int snippetCs = cursor.getColumnIndex("snippet_cs");
		int type = cursor.getColumnIndex("type");
		
		if (cursor != null) {
			while (cursor.moveToNext()) {
				ConversationInfo thread = new ConversationInfo();
				thread.setId(cursor.getInt(id));
				thread.setDate(cursor.getLong(date));
				thread.setError(cursor.getInt(error));
				thread.setHasAttachment(cursor.getInt(hasAttachment));
				thread.setMessageCount(cursor.getInt(messageCount));
				thread.setRead(cursor.getInt(read));
				thread.setSnippet(cursor.getString(snippet));
				thread.setSnippetCs(cursor.getInt(snippetCs));
				thread.setType(cursor.getInt(type));
				String rid = cursor.getString(recipientId);
				thread.setRecipientId(rid);
				String[] ids=rid.split(" ");			//获取联系人电话号码，判断是否群发
				if(ids.length > 1){
					thread.setIsMass(1);
					ContactInfo[] ctis = new ContactInfo[ids.length];
					for(int i=0; i<ids.length; i++){
						ctis[i] = new ContactInfo();
						ctis[i].setPhone(canonicalBuffer.get(ids[i]));
					}
					thread.setCtis(ctis);
				}else{
					ContactInfo cti = new ContactInfo();
					cti.setPhone(canonicalBuffer.get(ids[0]));
					thread.setCti(cti);
				}
				
				threads.add(thread);
			}
			cursor.close();
		}
		return threads;
	}

	public String getUnreadNum() {
		String num = "0";
		ContentResolver resolver = activity.getContentResolver();
		Cursor cursor = resolver.query(uri,
				new String[] { "COUNT(read) AS unReadNum" },
				"read = 0) GROUP BY (read", // GROUP BY
				null, null);
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			num = cursor.getString(cursor.getColumnIndex("unReadNum"));
		}
		cursor.close();
		return num;
	}
	
	public String[] getCanonicalAddressById(String recipientId){
        String[] ids=recipientId.split(" ");
        String[] addressResult = new String[ids.length];
        ContentResolver resolver = activity.getContentResolver();
        for (int i = 0; i < ids.length; i++) {
            Cursor cursor = resolver.query(Uri.parse(Uris.CANONICAL_URI_ADDRESS + ids[i]), 
                    null, null, null, null);
            if(cursor.getCount()>0){
                while (cursor.moveToNext()){
                    String address=cursor.getString(cursor.getColumnIndex("address"));
                    addressResult[i] = address;
                }
            }
            cursor.close();
        }
        return addressResult;
    }
	
	public int deleteConversation(int id){
		ContentResolver resolver = activity.getContentResolver();
		int result = resolver.delete(Uri.parse(Uris.CONVERSATION_URI + id), null, null);
		return result;
	}
	
}