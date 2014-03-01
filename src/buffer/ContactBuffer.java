package buffer;

import entity.ContactInfo;
import java.util.HashMap;

public class ContactBuffer {
	private HashMap<String, ContactInfo> cb;

	public ContactBuffer() {
		cb = new HashMap<String, ContactInfo>();
	}
	
	public HashMap<String, ContactInfo> getBuffer(){
		return cb;
	}
}
