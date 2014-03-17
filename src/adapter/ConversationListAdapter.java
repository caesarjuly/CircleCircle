package adapter;

import holder.ConversationHolder;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import ustc.wth.circlecircle.R;
import utils.ConvNameFormat;
import utils.TimeFormat;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import async.ConvPhotoAsyncLoader;
import entity.*;
import buffer.*;

public class ConversationListAdapter extends BaseAdapter {
	private List<ConversationInfo> conversations;
	Context c;
	private LayoutInflater layoutinflater;
	
	public ConversationListAdapter(Context c, List<ConversationInfo> conversations) {
		this.c = c;
		this.conversations = conversations;
		layoutinflater = LayoutInflater.from(c);
	}
	
	public void removeConversation(ConversationInfo ci){
		conversations.remove(ci);
	}
	
	/**
	 * notifyDataSetChanged，调用getview，显示改变后conversations的内容
	 */
	public void updateListView(List<ConversationInfo> list){
		this.conversations = list;
		notifyDataSetChanged();
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
		ConversationInfo convInfo = conversations.get(position);
		if (convertView == null) {
			convertView = layoutinflater.inflate(R.layout.conversation_line,
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
		

		holder.getBody().setText(convInfo.getSnippet());

		String time = TimeFormat.formatTimeStampString(c, convInfo.getDate());
		holder.getDate().setText(time);
		
		
		holder.setPosition(position);
		if (!convInfo.getIsMass()) { // 不是群发
			ContactInfo cti = convInfo.getCti();
			String phone = cti.getPhone();
			
			holder.getName().setText(phone);	//重置头像和名字，以防止重复显示
			holder.getImg().setText(null);
			holder.getImg().setBackgroundResource(R.drawable.ic_contact_picture);
			
			if (cti.getName() != null) {
				holder.getName().setText(cti.getName());
				String sb = cti.getName().substring(0, 1);
				holder.getImg().setText(sb);
				holder.getImg().setBackgroundColor(c.getResources()
						.getColor(R.color.lightgray));
			}
			
			if(convInfo.getIsLoaded()){  //被异步加载过		
				if(cti.getPhoto() != null){
					Drawable bd = new BitmapDrawable(c.getResources(),
							cti.getPhoto());
					holder.getImg().setText(null);
					holder.getImg().setBackgroundDrawable(bd);
				}
			}else{
				new ConvPhotoAsyncLoader(c, holder, position, 
						convInfo).execute();
			}
		} else {
			ContactInfo[] ctis = convInfo.getCtis();
			String name = ConvNameFormat.ConvNameFormat(ctis);
			if(!convInfo.getIsLoaded()){
				new ConvPhotoAsyncLoader(c, holder, position, 
						convInfo).execute();
			}
			holder.getName().setText(name);
			holder.getImg().setText(null);
			holder.getImg()
					.setBackgroundResource(R.drawable.ic_contacts_picture);
		}

		int mc = convInfo.getMessageCount();
		holder.getMessageCount().setText("(" + mc + ")");

		int read = convInfo.getRead();
		holder.getNotice().setVisibility(View.GONE);
		if (read == 0) {
			holder.getNotice().setVisibility(View.VISIBLE);
		}

		return convertView;
	}

}
