package service;

import java.util.ArrayList;
import java.util.List;
import entity.SmsInfo;
import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

/**
 * class name：SmsService
 * class description：获取手机中的各种短信信息
 * PS： 需要权限 <uses-permission android:name="android.permission.READ_SMS" />
 * Date:2014-2-21<BR>
 * 
 * @version 1.00
 * @author YF
 */
public class SmsService {
	private Activity activity;
	private Uri uri;
	private String[] projection = new String[] { "_id", "address", "person",
			"body", "date", "type" };
	private List<SmsInfo> infos;

	public SmsService(Activity activity, Uri uri) {
		infos = new ArrayList<SmsInfo>();
		this.activity = activity;
		this.uri = uri;
	}

	/**
	 * Role:获取短信的各种信息
	 * Date:2014-2-21
	 * 
	 */
	public List<SmsInfo> getSmsInfo() {
		
		ContentResolver resolver = activity.getContentResolver();
		//使用hack完成distinct查询，也可自己覆写provider实现
		Cursor cursor = resolver.query(uri, 
	            new String[]{"DISTINCT address", "body", "person", "date", "type"}, //DISTINCT
	            "address IS NOT NULL) GROUP BY (address", //GROUP BY
	            null, null);
		int nameColumn = cursor.getColumnIndex("person");
		int phoneNumberColumn = cursor.getColumnIndex("address");
		int smsbodyColumn = cursor.getColumnIndex("body");
		int dateColumn = cursor.getColumnIndex("date");
		int typeColumn = cursor.getColumnIndex("type");
		if (cursor != null) {
			while (cursor.moveToNext()) {
				SmsInfo smsinfo = new SmsInfo();
				smsinfo.setName(cursor.getString(nameColumn));
				smsinfo.setDate(cursor.getString(dateColumn));
				smsinfo.setPhoneNumber(cursor.getString(phoneNumberColumn));
				smsinfo.setSmsbody(cursor.getString(smsbodyColumn));
				smsinfo.setType(cursor.getString(typeColumn));
				infos.add(smsinfo);
			}
			cursor.close();
		}
		return infos;
	}
}