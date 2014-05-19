package async;

import java.util.HashMap;

import holder.CallHolder;
import holder.ConversationHolder;
import service.ContactService;
import ustc.wth.circlecircle.R;
import utils.ConvNameFormat;
import adapter.CallListAdapter;
import android.app.Activity;
import android.content.Context;
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
	Context ctx;

	public CallPhotoAsyncLoader(Context context,
			CallHolder holder, int position, CallInfo callInfo) {
		this.holder = holder;
		this.position = position;
		this.ctx = context;
		this.callInfo = callInfo;
		cts = new ContactService((Activity) ctx);
	}

	@Override
	protected CallInfo doInBackground(Void...params) {
		callInfo.setName(cts.getContactNameByPhone(callInfo.getPhone()));
		callInfo.setPhoto(cts.getConvPhotoByPhone(callInfo.getPhone()));
		callInfo.setLoaded(true);
		return callInfo;

	}

	@Override
	protected void onPostExecute(CallInfo result) {
		if (position == holder.getPosition()) {
				if (result.getPhoto() != null) {
					Drawable bd = new BitmapDrawable(ctx.getResources(),
							result.getPhoto());
					holder.getImg().setText(null);
					holder.getImg().setBackgroundDrawable(bd);
				}
				if(result.getName()!=null){
					holder.getName().setText(result.getName());
					holder.getBody().setText(result.getPhone());
				}
				

			}
	}
}
