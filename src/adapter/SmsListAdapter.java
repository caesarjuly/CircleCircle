package adapter;

import holder.SmsHolder;

import java.util.HashMap;
import java.util.List;

import buffer.Hash;
import buffer.NameBuffer;
import ustc.wth.circlecircle.R;
import utils.TimeFormat;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.opengl.Visibility;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import entity.*;

public class SmsListAdapter extends BaseAdapter {
	private List<SmsInfo> smsList;
	Context c;
	private LayoutInflater layoutinflater;
	private Hash<String> nb;
	private boolean isMass;

	public SmsListAdapter(Context c, List<SmsInfo> smsList, boolean isMass) {
		this.c = c;
		this.smsList = smsList;
		this.isMass = isMass;
		nb = NameBuffer.getInstance();
		layoutinflater = LayoutInflater.from(c);
	}

	/**
	 * notifyDataSetChanged，调用getview，显示改变后conversations的内容
	 */
	public void updateListView(List<SmsInfo> list) {
		this.smsList = list;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return smsList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return smsList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return smsList.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		SmsHolder holder;
		SmsInfo smsInfo = smsList.get(position);
		if (convertView == null) {
			convertView = layoutinflater.inflate(R.layout.sms_line, null);
			holder = new SmsHolder();
			holder.setSms_line((LinearLayout) convertView.findViewById(R.id.sms_line));
			holder.setSms_title((LinearLayout) convertView.findViewById(R.id.sms_title));
			holder.setSms_body((TextView) convertView
					.findViewById(R.id.sms_body));
			holder.setSms_date((TextView) convertView
					.findViewById(R.id.sms_date));
			holder.setSms_status((ImageView) convertView
					.findViewById(R.id.sms_status));

			convertView.setTag(holder);
		} else {
			holder = (SmsHolder) convertView.getTag();
		}
		holder.getSms_line().setGravity(Gravity.LEFT);
		holder.getSms_title().setGravity(Gravity.LEFT);
		holder.getSms_body().setBackgroundResource(R.drawable.bg_message_other_side_selected_pressed);
		
		if (smsInfo.getType() == 2) {
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			layoutParams.gravity = Gravity.RIGHT;
			holder.getSms_line().setGravity(Gravity.RIGHT);
			holder.getSms_title().setGravity(Gravity.RIGHT);
			holder.getSms_body().setBackgroundResource(R.drawable.bg_message_my_side_selected_pressed);
			
		}
		String address = nb.get(smsInfo.getAddress());
		if(address == null){
			address = smsInfo.getAddress();
		}
		switch(smsInfo.getStatus()){
		case 64:
			holder.getSms_status().setImageResource(R.drawable.about_load);
			if(isMass){
				holder.getSms_date().setText("正在发送给 " + address);
			}else{
				holder.getSms_date().setText("正在发送...");
			}
			holder.getSms_status().setVisibility(View.VISIBLE);
			break;
		case 128:
			holder.getSms_status().setImageResource(R.drawable.ic_sms_mms_fail);
			holder.getSms_status().setVisibility(View.VISIBLE);
			break;
		case 1:
			holder.getSms_status().setImageResource(R.drawable.ic_sms_mms_delivered);
			holder.getSms_status().setVisibility(View.VISIBLE);
			break;
		default:
			holder.getSms_status().setVisibility(View.INVISIBLE);
			String date = TimeFormat.formatTimeStampString(c, smsInfo.getDate());
			if(isMass){
				holder.getSms_date().setText(address + " " + date);
			}else{
				holder.getSms_date().setText(date);
			}
			break;
		}
		
		holder.getSms_body().setText(smsInfo.getBody());

		return convertView;

	}

	public void removeSms(SmsInfo smsInfo) {
		// TODO Auto-generated method stub
		smsList.remove(smsInfo);
	}
	
	
}
