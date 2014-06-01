package async;

import java.util.HashMap;

import buffer.Hash;
import buffer.PhotoBuffer;

import holder.CallHolder;
import holder.ConversationHolder;
import service.ContactService;
import ustc.wth.circlecircle.R;
import utils.ConvNameFormat;
import adapter.CallListAdapter;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import entity.CallInfo;
import entity.ContactInfo;
import entity.ConversationInfo;

public class CallPhotoAsyncLoader extends AsyncTask<Void, Void, CallInfo> {
	private ContactService cts;
	private CallHolder holder;
	private int position;
	private CallInfo callInfo;
	private Hash<Bitmap> pb;
	Context ctx;

	public CallPhotoAsyncLoader(Context context,
			CallHolder holder, int position, CallInfo callInfo) {
		this.holder = holder;
		this.position = position;
		this.ctx = context;
		this.callInfo = callInfo;
		pb = PhotoBuffer.getInstance();
		cts = new ContactService();
	}

	@Override
	protected CallInfo doInBackground(Void...params) {
		Bitmap photo = cts.getConvPhotoByPhone(callInfo.getPhone());
		pb.put(callInfo.getPhone(), photo);
		return callInfo;

	}

	@Override
	protected void onPostExecute(CallInfo result) {
		if (position == holder.getPosition()) {
				if (pb.get(result.getPhone()) != null) {
					Drawable bd = new BitmapDrawable(ctx.getResources(),
							pb.get(result.getPhone()));
					holder.getImg().setText(null);
					holder.getImg().setBackgroundDrawable(bd);
				}			

			}
	}
}
