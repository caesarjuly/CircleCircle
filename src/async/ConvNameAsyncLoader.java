package async;

import java.util.List;

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

	public ConvNameAsyncLoader(Context context, ConversationListAdapter smsListAdapter,
			List<ConversationInfo> conversations) {
		this.ctx = context;
		this.conversations = conversations;
		this.smsListAdapter = smsListAdapter;
		cts = new ContactService((Activity) ctx);
	}

	@Override
	protected Void doInBackground(Void... params) {
		for (ConversationInfo conversation : conversations) {
			if (!conversation.getIsMass()) {
				ContactInfo ci = conversation.getCti();
				ci.setName(cts.getContactNameByPhone(ci.getPhone()));
			} else {
				ContactInfo[] cis = conversation.getCtis();
				for (int i = 0; i < cis.length; i++) {
					cis[i].setName(cts.getContactNameByPhone(cis[i].getPhone()));
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
