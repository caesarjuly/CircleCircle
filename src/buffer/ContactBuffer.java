 	package buffer;
 	
	import entity.ContactInfo;
	
	import java.util.HashMap;
	
	import utils.Uris;
	
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
			cd = new HashMap<String, String>();
			this.activity = activity;
		}
		
		
		public HashMap<String, String> getcontactBuffer(){
			return cd;
		}
		
		public HashMap<String, ContactInfo> getBuffer(){
			return cb;
		}
 	}

