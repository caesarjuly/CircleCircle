package async;

import java.util.HashMap;

import buffer.ContactBuffer;
import holder.ConversationHolder;
import service.ContactService;
import ustc.wth.circlecircle.R;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import entity.ContactInfo;
import entity.ConversationInfo;

public class ContactAsyncLoader extends AsyncTask<Integer, Void, ContactInfo[]> {
	private ContactService cts;
	private ConversationHolder holder;
	private int position;
	private ConversationInfo conversation;
	Context ctx;

	public ContactAsyncLoader(Context context, ConversationHolder holder,
			int position, ConversationInfo conversation) {
		this.holder = holder;
		this.position = position;
		this.ctx = context;
		this.conversation = conversation;
		cts = new ContactService((Activity) ctx);
	}

	@Override
	protected ContactInfo[] doInBackground(Integer... isMass) {
		ContactInfo[] ctis;
		String phone;
		if (isMass[0] == 0) {
			ctis = new ContactInfo[1];
			phone = conversation.getCti().getPhone();
			ctis[0] = cts.getContactByPhone(phone);
			conversation.setCti(ctis[0]);
		} else {
			ctis = conversation.getCtis();
			for (int i = 0; i < ctis.length; i++) {
				phone = ctis[i].getPhone();
				ctis[i] = cts.getContactByPhone(phone);
			}
			conversation.setCtis(ctis);
		}
		conversation.setIsLoaded(true);

		return ctis;
	}

	@Override
	protected void onPostExecute(ContactInfo[] result) {

		super.onPostExecute(result);
		if (position == holder.getPosition()) {
			if (conversation.getIsMass() == 0) {
				if (result[0].getName() != null) {
					holder.getName().setText(result[0].getName());
					String sb = result[0].getName().substring(0, 1);
					holder.getImg().setText(sb);
					holder.getImg().setBackgroundColor(
							ctx.getResources().getColor(R.color.lightgray));
				}
				if (result[0].getPhoto() != null) {
					Drawable bd = new BitmapDrawable(ctx.getResources(),
							result[0].getPhoto());
					holder.getImg().setText(null);
					holder.getImg().setBackgroundDrawable(bd);
				}

			} else {
				String name = "";
				int i;
				for (i = 0; i < result.length - 1; i++) {
					if (result[i].getName() == null) {
						name += result[i].getPhone() + ",";
					} else {
						name += result[i].getName() + ",";
					}

				}
				if (result[i].getName() == null) {
					name += result[i].getPhone();
				} else {
					name += result[i].getName();
				}

				holder.getName().setText(name);
			}

		}
	}
}
