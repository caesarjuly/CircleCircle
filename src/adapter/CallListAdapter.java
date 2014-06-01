package adapter;

import holder.CallHolder;
import holder.ConversationHolder;

import java.util.HashMap;
import java.util.List;

import buffer.Hash;
import buffer.PhotoBuffer;

import ustc.wth.circlecircle.R;
import utils.TimeFormat;

import entity.CallInfo;
import entity.ConversationInfo;
import entity.SmsInfo;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import async.CallPhotoAsyncLoader;
import async.ConvPhotoAsyncLoader;

public class CallListAdapter extends BaseAdapter {
	private List<CallInfo> callList;
	Context c;
	private LayoutInflater layoutinflater;
	private Hash<Bitmap> pb;

	public CallListAdapter(Context c, List<CallInfo> callList) {
		this.c = c;
		this.callList = callList;
		layoutinflater = LayoutInflater.from(c);
		pb = PhotoBuffer.getInstance();
	}

	/**
	 * notifyDataSetChanged，调用getview，显示改变后conversations的内容
	 */
	public void updateListView(List<CallInfo> list) {
		this.callList = list;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return callList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return callList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return callList.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		CallHolder holder;
		CallInfo callInfo = callList.get(position);
		if (convertView == null) {
			convertView = layoutinflater.inflate(R.layout.home_line, null);
			holder = new CallHolder();
			holder.setBody((TextView) convertView.findViewById(R.id.body));
			holder.setName((TextView) convertView.findViewById(R.id.name));
			holder.setDate((TextView) convertView.findViewById(R.id.date));
			holder.setImg((TextView) convertView.findViewById(R.id.image));
			holder.setCall_image((ImageView) convertView
					.findViewById(R.id.call_image));
			holder.setIn_out((ImageView) convertView.findViewById(R.id.in_out));

			convertView.setTag(holder);
		} else {
			holder = (CallHolder) convertView.getTag();
		}

		holder.getName().setText(callInfo.getName());
		holder.getBody().setText(callInfo.getPhone());
		holder.getCall_image().setBackgroundResource(R.drawable.call_contact);
		String time = TimeFormat.formatTimeStampString(c, callInfo.getDate());
		holder.getDate().setText(time);

		switch (callInfo.getType()) {
		case 1:
			holder.getIn_out().setBackgroundResource(
					R.drawable.sym_call_incoming);
			break;
		case 2:
			holder.getIn_out().setBackgroundResource(
					R.drawable.sym_call_outgoing);
			break;
		case 3:
			holder.getIn_out()
					.setBackgroundResource(R.drawable.sym_call_missed);
			break;
		}
		
		holder.setPosition(position);
		holder.getCall_image().setOnClickListener(new CallListener(callInfo.getPhone()));
		 if (callInfo.getName() != null) {
				holder.getName().setText(callInfo.getName());
				String sb = callInfo.getName().substring(0, 1);
				holder.getImg().setText(sb);
				holder.getImg().setBackgroundColor(
						c.getResources().getColor(R.color.lightgray));
			} else {
				holder.getImg().setText(null);
				holder.getImg().setBackgroundResource(
						R.drawable.ic_contact_picture);
				holder.getBody().setText(null);
				holder.getName().setText(callInfo.getPhone());
			}
		 
		if (pb.contains(callInfo.getPhone())) { // 被异步加载过
			if (pb.get(callInfo.getPhone()) != null) {
				Drawable bd = new BitmapDrawable(c.getResources(),
						pb.get(callInfo.getPhone()));
				holder.getImg().setText(null);
				holder.getImg().setBackgroundDrawable(bd);
			}
		} else {
			new CallPhotoAsyncLoader(c, holder, position, callInfo).execute();
		}
		return convertView;

	}

	public void removeCall(CallInfo ci) {
		// TODO Auto-generated method stub
		callList.remove(ci);
	}
	class CallListener implements OnClickListener {

		private String phone;

		CallListener(String phone) {
			this.phone = phone;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
					+ phone));
			c.startActivity(intent);
		}

	}

}


