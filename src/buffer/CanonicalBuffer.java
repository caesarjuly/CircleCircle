package buffer;

import java.util.HashMap;

import utils.Uris;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

public class CanonicalBuffer extends Buffer implements Hash<String> {
	private static CanonicalBuffer cb;

	private CanonicalBuffer() {
		hashBuffer = new HashMap<String, String>();
		initBuffer();
	}

	public static CanonicalBuffer getInstance() {
		if (cb == null) {
			cb = new CanonicalBuffer();
		}
		return cb;
	}

	public void initBuffer() {
		ContentResolver resolver = activity.getContentResolver();
		Cursor cursor = resolver.query(Uri.parse(Uris.CANONICAL_URI_ADDRESSES),
				null, null, null, null);
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				String id = cursor.getString(cursor.getColumnIndex("_id"));
				String address = cursor.getString(cursor
						.getColumnIndex("address"));
				hashBuffer.put(id, address);
			}
		}
		cursor.close();
	}

	@Override
	public boolean contains(String key) {
		// TODO Auto-generated method stub
		return hashBuffer.containsKey(key);
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

	@Override
	public void put(String key, String value) {
		// TODO Auto-generated method stub
		hashBuffer.put(key, value);
	}
}
