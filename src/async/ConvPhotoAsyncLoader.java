package async;

import java.util.HashMap;

import buffer.Hash;
import buffer.PhotoBuffer;

import holder.ConversationHolder;
import service.ContactService;
import ustc.wth.circlecircle.R;
import utils.ConvNameFormat;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
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
	private Hash<Bitmap> pb;
	Context ctx;

	public ConvPhotoAsyncLoader(Context context, ConversationHolder holder,
			int position, ConversationInfo conversation) {
		this.holder = holder;
		this.position = position;
		this.ctx = context;
		this.conversation = conversation;
		pb = PhotoBuffer.getInstance();
		cts = new ContactService();
	}

	@Override
	protected ContactInfo doInBackground(Void...params) {
		ContactInfo cti = null;
		String phone;
		if (!conversation.getIsMass()) {
			cti = conversation.getCti();
			phone = cti.getPhone();
			Bitmap photo = cts.getConvPhotoByPhone(phone);
			pb.put(cti.getPhone(), photo);
		}

		return cti;
	}

	@Override
	protected void onPostExecute(ContactInfo result) {

		super.onPostExecute(result);
		if (position == holder.getPosition()) {
			if (!conversation.getIsMass()) {
				if (pb.get(result.getPhone()) != null) {
					Drawable bd = new BitmapDrawable(ctx.getResources(),
							pb.get(result.getPhone()));
					holder.getImg().setText(null);
					holder.getImg().setBackgroundDrawable(bd);
				}

			}

		}
	}
}
