package buffer;

import global.Uris;

import java.util.HashMap;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

public class CanonicalBuffer {
	private HashMap<String, String> cb;
	private Activity activity;

	public CanonicalBuffer(Activity activity) {
		cb = new HashMap<String, String>();
		this.activity = activity;
		initBuffer();
	}

	private void initBuffer() {
		ContentResolver resolver = activity.getContentResolver();
		Cursor cursor = resolver.query(
				Uri.parse(Uris.CANONICAL_URI_ADDRESSES), null, null, null,
				null);
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				String id = cursor.getString(cursor.getColumnIndex("_id")); 
				String address = cursor.getString(cursor.getColumnIndex("address"));
				cb.put(id, address);
				}
		}
		cursor.close();
	}
	
	public HashMap<String, String> getBuffer(){
		return cb;
	}
}
