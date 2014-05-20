package async;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import service.ContactService;
import adapter.ConversationListAdapter;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import entity.ContactInfo;
import entity.ConversationInfo;

public class ConvNameAsyncLoader extends AsyncTask<Void, Void, Void> {
	private ContactService cts;
	private List<ConversationInfo> conversations;
	private ConversationListAdapter smsListAdapter;
	Context ctx;
	static Map<String, String> phoneToName  = new HashMap<String, String>();

	public ConvNameAsyncLoader(Context context, ConversationListAdapter smsListAdapter,
			List<ConversationInfo> conversations) {
		this.ctx = context;
		this.conversations = conversations;
		this.smsListAdapter = smsListAdapter;
		cts = new ContactService((Activity) ctx);
	}

	@Override
	protected Void doInBackground(Void... params) {
		String name;
		for (ConversationInfo conversation : conversations) {
			if (!conversation.getIsMass()) {
				ContactInfo ci = conversation.getCti();
				if(phoneToName.containsKey(ci.getPhone())){
					ci.setName(phoneToName.get(ci.getPhone()));
				}else{
					name = cts.getContactNameByPhone(ci.getPhone());
					phoneToName.put(ci.getPhone(), name);
					ci.setName(name);
				}
			} else {
				ContactInfo[] cis = conversation.getCtis();
				for (int i = 0; i < cis.length; i++) {
					if(phoneToName.containsKey(cis[i].getPhone())){
						cis[i].setName(phoneToName.get(cis[i].getPhone()));
					}else{
						name = cts.getContactNameByPhone(cis[i].getPhone());
						phoneToName.put(cis[i].getPhone(), name);
						cis[i].setName(name);
					}
				}
			}
		}
		return null;
	}

	@Override
	protected void onPostExecute(Void param) {
		smsListAdapter.notifyDataSetChanged();
	}
}
