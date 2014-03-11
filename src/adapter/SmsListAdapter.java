package adapter;

import holder.ConversationHolder;

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
	private List<ConversationInfo> conversations;
	Context c;
	private LayoutInflater layoutinflater;
	private ContactBuffer contactBuffer;

	public SmsListAdapter(Context c, List<ConversationInfo> conversations) {
		this.c = c;
		this.conversations = conversations;
		this.contactBuffer = new ContactBuffer();
		layoutinflater = LayoutInflater.from(c);
	}
	
	public void removeConversation(ConversationInfo ci){
		conversations.remove(ci);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return conversations.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return conversations.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return conversations.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ConversationHolder holder;
		if (convertView == null) {
			convertView = layoutinflater.inflate(R.layout.message_line,
					null);
			holder = new ConversationHolder();
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
			holder = (ConversationHolder) convertView.getTag();
		}

		holder.getBody().setText(conversations.get(position).getSnippet());

		String time = TimeFormat.formatTimeStampString(c, conversations.get(position).getDate());
		holder.getDate().setText(time);

		if (conversations.get(position).getIsMass() == 0) { // 不是群发
			ContactInfo cti = conversations.get(position).getCti();
			String phone = cti.getPhone();
			holder.getName().setText(phone);
			holder.getImg().setText(null);
			holder.getImg().setBackgroundResource(R.drawable.ic_contact_picture);

		} else {
			int i;
			ContactInfo[] ctis = conversations.get(position).getCtis();
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
				conversations.get(position), contactBuffer).execute(conversations.get(position).getIsMass());

		int mc = conversations.get(position).getMessageCount();
		holder.getMessageCount().setText("(" + mc + ")");

		int read = conversations.get(position).getRead();
		holder.getNotice().setVisibility(View.GONE);
		if (read == 0) {
			holder.getNotice().setVisibility(View.VISIBLE);
		}

		return convertView;
	}

}
