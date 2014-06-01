package buffer;

import java.util.HashMap;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds;

public class NameBuffer extends Buffer implements Hash<String> {
	private static NameBuffer cb;

	private NameBuffer() {
		hashBuffer = new HashMap<String, String>();
		initBuffer();
	}

	public static NameBuffer getInstance() {
		if (cb == null) {
			cb = new NameBuffer();
		}
		return cb;
	}

	private void initBuffer() {
		ContentResolver resolver = activity.getContentResolver();
		Cursor cursor = resolver.query(
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
				new String[] { CommonDataKinds.Phone.NUMBER,
						CommonDataKinds.Phone.DISPLAY_NAME }, null, null, null);

		while (cursor.moveToNext()) {

			String phone = cursor
					.getString(cursor
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
			String name = cursor.getString(cursor
					.getColumnIndex(CommonDataKinds.Phone.DISPLAY_NAME));
			hashBuffer.put(phone, name);

		}

		cursor.close();
	}

	@Override
	public boolean contains(String key) {
		// TODO Auto-generated method stub
		return hashBuffer.containsKey(key);
	}

	@Override
	public void put(String key, String value) {
		// TODO Auto-generated method stub
		hashBuffer.put(key, value);
	}

	@Override
	public String get(String key) {
		// TODO Auto-generated method stub
		return (String) hashBuffer.get(key);
	}

	@Override
	public String remove(String key) {
		// TODO Auto-generated method stub
		return (String) hashBuffer.remove(key);
	}

}
