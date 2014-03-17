package adapter;

import holder.SmsHolder;

import java.util.List;
import ustc.wth.circlecircle.R;
import utils.TimeFormat;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import entity.*;

public class SmsListAdapter extends BaseAdapter {
	private List<SmsInfo> smsList;
	Context c;
	private LayoutInflater layoutinflater;

	public SmsListAdapter(Context c, List<SmsInfo> smsList) {
		this.c = c;
		this.smsList = smsList;
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
		if (smsInfo.getType() == 2) {
			RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			holder.getSms_body().setLayoutParams(
					new RelativeLayout.LayoutParams(layoutParams));
			holder.getSms_date().setLayoutParams(
					new RelativeLayout.LayoutParams(layoutParams));
			holder.getSms_status().setLayoutParams(
					new RelativeLayout.LayoutParams(layoutParams));
		}
		
		String date = TimeFormat.formatTimeStampString(c, smsInfo.getDate());
		holder.getSms_date().setText(date);
		
		holder.getSms_body().setText(smsInfo.getBody());
		

		return convertView;

	}

}
