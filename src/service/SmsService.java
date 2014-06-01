package service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import utils.ContextUtil;
import utils.Uris;

import buffer.CanonicalBuffer;
import buffer.Hash;
import buffer.NameBuffer;

import entity.ContactInfo;
import entity.ConversationInfo;
import entity.SmsInfo;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
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
	private ContextUtil activity;

	public SmsService() {
		this.activity = ContextUtil.getInstance();

	}



	public List<SmsInfo> getSmsByConvId(long Convid) {
		String[] projection = new String[] { "_id", "address", "person",
				"body", "type", "date", "status" };
		List<SmsInfo> smsList = new ArrayList<SmsInfo>();
		ContentResolver resolver = activity.getContentResolver();
		Cursor cursor = resolver.query(Uri.parse(Uris.SMS_URI_ALL), projection,
				"thread_id=?", new String[] { Long.toString(Convid) },
				"date ASC");
		int idColumn = cursor.getColumnIndex("_id");
		int addressColumn = cursor.getColumnIndex("address");
		int personColumn = cursor.getColumnIndex("person");
		int bodyColumn = cursor.getColumnIndex("body");
		int dateColumn = cursor.getColumnIndex("date");
		int typeColumn = cursor.getColumnIndex("type");
		int statusColumn = cursor.getColumnIndex("status");
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				SmsInfo sms = new SmsInfo();
				sms.setId(cursor.getLong(idColumn));
				sms.setAddress(cursor.getString(addressColumn));
				sms.setPerson(cursor.getString(personColumn));
				sms.setBody(cursor.getString(bodyColumn));
				sms.setDate(cursor.getLong(dateColumn));
				sms.setType(cursor.getInt(typeColumn));
				sms.setStatus(cursor.getInt(statusColumn));
				smsList.add(sms);
			}
		}
		cursor.close();
		return smsList;
	}

	public Uri addSms(SmsInfo sms) {
		// 写入到短信数据源
		ContentValues values = new ContentValues();
		values.put("address", sms.getAddress()); // 发送地址
		values.put("body", sms.getBody()); // 消息内容
		values.put("date", sms.getDate()); // 创建时间
		values.put("read", sms.getRead()); // 0:未读;1:已读
		values.put("type", sms.getType()); // 1:接收;2:发送
		values.put("thread_id", sms.getThread_id());
		values.put("status", sms.getStatus());
		return activity.getContentResolver().insert(
				Uri.parse("content://sms/sent"), values); // 插入数据
	}

	public int update(String uri) {
		ContentValues values = new ContentValues();
		values.put("status", 0);
		return activity.getContentResolver().update(Uri.parse(uri), values,
				null, null);
	}

	public int deleteSms(long l) {
		ContentResolver resolver = activity.getContentResolver();
		int result = resolver.delete(Uri.parse(Uris.SMS_URI_ALL + l), null,
				null);
		return result;
	}

	public void markUnread(long Convid) {
		ContentValues cv = new ContentValues();
		cv.put("read", 1);
		ContentResolver resolver = activity.getContentResolver();
		int result = resolver.update(Uri.parse(Uris.SMS_URI_ALL), cv,
				"thread_id=? and read=?", new String[] { Long.toString(Convid),
						"0" });
	}

}