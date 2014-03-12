package async;

import java.util.HashMap;

import holder.ConversationHolder;
import service.ContactService;
import ustc.wth.circlecircle.R;
import utils.ConvNameFormat;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import entity.ContactInfo;
import entity.ConversationInfo;

public class ConvPhotoAsyncLoader extends AsyncTask<Void, Void, ContactInfo> {
	private ContactService cts;
	private ConversationHolder holder;
	private int position;
	private ConversationInfo conversation;
	Context ctx;

	public ConvPhotoAsyncLoader(Context context, ConversationHolder holder,
			int position, ConversationInfo conversation) {
		this.holder = holder;
		this.position = position;
		this.ctx = context;
		this.conversation = conversation;
		cts = new ContactService((Activity) ctx);
	}

	@Override
	protected ContactInfo doInBackground(Void...params) {
		ContactInfo ctis = null;
		String phone;
		if (!conversation.getIsMass()) {
			ctis = conversation.getCti();
			phone = conversation.getCti().getPhone();
			ctis.setPhoto(cts.getConvPhotoByPhone(phone));
		}
		conversation.setIsLoaded(true);

		return ctis;
	}

	@Override
	protected void onPostExecute(ContactInfo result) {

		super.onPostExecute(result);
		if (position == holder.getPosition()) {
			if (!conversation.getIsMass()) {
				if (result.getPhoto() != null) {
					Drawable bd = new BitmapDrawable(ctx.getResources(),
							result.getPhoto());
					holder.getImg().setText(null);
					holder.getImg().setBackgroundDrawable(bd);
				}

			}

		}
	}
}
