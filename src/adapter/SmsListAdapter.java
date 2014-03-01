package adapter;

import holder.ThreadHolder;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import ustc.wth.circlecircle.R;
import utils.TimeFormat;
import android.content.Context;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import async.ContactAsyncLoader;
import entity.*;
import buffer.*;

public class SmsListAdapter extends BaseAdapter {
	private List<ThreadInfo> threads;
	Context c;
	private LayoutInflater layoutinflater;
	private ContactBuffer contactBuffer;

	public SmsListAdapter(Context c, List<ThreadInfo> threads) {
		this.c = c;
		this.threads = threads;
		this.contactBuffer = new ContactBuffer();
		layoutinflater = LayoutInflater.from(c);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return threads.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return threads.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return threads.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ThreadHolder holder;
		if (convertView == null) {
			convertView = layoutinflater.inflate(R.layout.message_line,
					null);
			holder = new ThreadHolder();
			holder.setBody((TextView) convertView.findViewById(R.id.body));
			holder.setName((TextView) convertView.findViewById(R.id.name));
			holder.setMessageCount((TextView) convertView
					.findViewById(R.id.messageCount));
			holder.setDate((TextView) convertView.findViewById(R.id.date));
			holder.setImg((TextView) convertView.findViewById(R.id.image));
			holder.setNotice((ImageView) convertView
					.findViewById(R.id.notice));

			convertView.setTag(holder);
		} else {
			holder = (ThreadHolder) convertView.getTag();
		}

		holder.getBody().setText(threads.get(position).getSnippet());

		String time = TimeFormat.formatTimeStampString(c, threads.get(position).getDate());
		holder.getDate().setText(time);

		if (threads.get(position).getIsMass() == 0) { // 不是群发
			ContactInfo cti = threads.get(position).getCti();
			String phone = cti.getPhone();
			holder.getName().setText(phone);
			holder.getImg().setText(null);
			holder.getImg().setBackgroundResource(R.drawable.ic_contact_picture);

		} else {
			int i;
			ContactInfo[] ctis = threads.get(position).getCtis();
			String name = "";
			for (i = 0; i < ctis.length - 1; i++) {
				name += ctis[i].getPhone() + ",";
			}
			name += ctis[i].getPhone();

			holder.getName().setText(name);
			holder.getImg().setText(null);
			holder.getImg()
					.setBackgroundResource(R.drawable.ic_contacts_picture);
		}
		holder.setPosition(position);
		new ContactAsyncLoader(c, holder, position, 
				threads.get(position), contactBuffer).execute(threads.get(position).getIsMass());

		int mc = threads.get(position).getMessageCount();
		holder.getMessageCount().setText("(" + mc + ")");

		int read = threads.get(position).getRead();
		holder.getNotice().setVisibility(View.GONE);
		if (read == 0) {
			holder.getNotice().setVisibility(View.VISIBLE);
		}

		return convertView;
	}

}
