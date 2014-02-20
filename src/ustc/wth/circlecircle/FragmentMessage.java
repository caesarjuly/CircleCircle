package ustc.wth.circlecircle;

import java.util.ArrayList;
import java.util.HashMap;

import android.support.v4.app.ListFragment;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class FragmentMessage extends ListFragment {

	private Uri Contacts = Uri.parse("content://com.android.contacts/contacts"); 
	private Uri SMS = Uri.parse("content://sms/");  

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_message, container, false);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setListAdapter(new SimpleAdapter(this.getActivity(), readAllContacts(),
				 R.layout.message_line,
				 new String[] {"name", "email", "phone", "body", "date"},
	             new int[] {R.id.name, R.id.email, R.id.phone, R.id.body, R.id.date}));

	}

	private ArrayList<HashMap<String, String>> readAllContacts() {
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>> ();
		Uri Contacts = Uri.parse("content://com.android.contacts/contacts");
		
		ContentResolver resolver = this.getActivity().getContentResolver();
		Cursor cursor = resolver.query(Contacts, new String[] { "_id" }, null, null,
				null);
		
		while (cursor.moveToNext()) {
			HashMap<String, String> item = new HashMap<String, String>();
			int contractID = cursor.getInt(0);
			Uri uri = Uri.parse("content://com.android.contacts/contacts/"
					+ contractID + "/data");
			Cursor cursorSingleContact = resolver.query(uri, new String[] { "mimetype",
					"data1", "data2" }, null, null, null);
			while (cursorSingleContact.moveToNext()) {
				String data1 = cursorSingleContact.getString(cursorSingleContact
						.getColumnIndex("data1"));
				String mimeType = cursorSingleContact.getString(cursorSingleContact
						.getColumnIndex("mimetype"));
				if ("vnd.android.cursor.item/name".equals(mimeType)) { // –’√˚
					item.put("name", data1);
				} else if ("vnd.android.cursor.item/email_v2".equals(mimeType)) { // ” œ‰
					item.put("email", data1);
				} else if ("vnd.android.cursor.item/phone_v2".equals(mimeType)) { //  ÷ª˙
					item.put("phone", data1);
					Cursor cursorSMS = resolver.query(SMS, new String[] { "body",
							"date"}, "address=?", new String[]{data1}, "date desc");
					if(cursorSMS.moveToNext()){
						String body = cursorSMS.getString(cursorSMS.getColumnIndex("body"));
						String date = cursorSMS.getString(cursorSMS.getColumnIndex("date"));
						item.put("body", body);
						item.put("date", date);
					}
					
				}
			}
			cursorSingleContact.close();
			list.add(item);
		}
		cursor.close();
		return list;
	}

	public void onListItemClick(ListView parent, View v, int position, long id) {
		Toast.makeText(getActivity(),
				"You have selected ", Toast.LENGTH_SHORT)
				.show();
	}

}