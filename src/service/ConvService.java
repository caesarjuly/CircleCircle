package service;

import java.util.ArrayList;
import java.util.List;

import entity.ContactInfo;
import entity.ConversationInfo;
import utils.ContextUtil;
import utils.Uris;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import buffer.CanonicalBuffer;
import buffer.Hash;
import buffer.NameBuffer;

public class ConvService {
	private ContextUtil activity;
	private Uri uri;
	private Hash<String> cb;
	private Hash<String> nb;

	public ConvService() {
		this.activity = ContextUtil.getInstance();
		this.uri = Uri.parse(Uris.SMS_URI_ALL);
		cb = CanonicalBuffer.getInstance();
		nb = NameBuffer.getInstance();
	}
	
	/**
	 * Role:获取短信的各种信息 Date:2014-2-21
	 * 
	 */
	public List<ConversationInfo> getConvInfo() {
		String phone;
		List<ConversationInfo> conversations = new ArrayList<ConversationInfo>();
		ContentResolver resolver = activity.getContentResolver();
		// 使用hack完成distinct查询，也可自己覆写provider实现
		// Cursor cursor = resolver.query(uri, new String[] {
		// "DISTINCT conversation_id", "COUNT(conversation_id) AS dialogNum",
		// "address", "body", "person", "date", "type", "read" }, // DISTINCT
		// "address IS NOT NULL) GROUP BY (conversation_id", // GROUP BY
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

		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				ConversationInfo conversation = new ConversationInfo();
				conversation.setId(cursor.getInt(id));
				conversation.setDate(cursor.getLong(date));
				conversation.setError(cursor.getInt(error));
				conversation.setHasAttachment(cursor.getInt(hasAttachment));
				conversation.setMessageCount(cursor.getInt(messageCount));
				conversation.setRead(cursor.getInt(read));
				conversation.setSnippet(cursor.getString(snippet));
				conversation.setSnippetCs(cursor.getInt(snippetCs));
				conversation.setType(cursor.getInt(type));
				String rid = cursor.getString(recipientId);
				conversation.setRecipientId(rid);
				String[] ids = rid.split(" "); // 获取联系人电话号码，判断是否群发
				if (ids.length > 1) {
					conversation.setIsMass(true);
					ContactInfo[] ctis = new ContactInfo[ids.length];
					for (int i = 0; i < ids.length; i++) {
						ctis[i] = new ContactInfo();
						phone = cb.get(ids[i]);
						if(phone.contains("+86")){
							phone = phone.substring(3, phone.length());
						}
						ctis[i].setPhone(phone);
						ctis[i].setName(nb.get(phone));
					}
					conversation.setCtis(ctis);
				} else {
					ContactInfo cti = new ContactInfo();
					phone = cb.get(ids[0]);
					if(phone.contains("+86")){
						phone = phone.substring(3, phone.length());
					}
					cti.setPhone(phone);
					cti.setName(nb.get(phone));
					conversation.setCti(cti);
				}

				conversations.add(conversation);
			}
			cursor.close();
		}
		return conversations;
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

	public String[] getCanonicalAddressById(String recipientId) {
		String[] ids = recipientId.split(" ");
		String[] addressResult = new String[ids.length];
		ContentResolver resolver = activity.getContentResolver();
		for (int i = 0; i < ids.length; i++) {
			Cursor cursor = resolver.query(
					Uri.parse(Uris.CANONICAL_URI_ADDRESS + ids[i]), null, null,
					null, null);
			if (cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					String address = cursor.getString(cursor
							.getColumnIndex("address"));
					addressResult[i] = address;
				}
			}
			cursor.close();
		}
		return addressResult;
	}

	public int deleteConversation(long id) {
		ContentResolver resolver = activity.getContentResolver();
		int result = resolver.delete(Uri.parse(Uris.CONVERSATION_URI + id),
				null, null);
		return result;
	}
}
