package adapter;

import holder.ConversationHolder;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import ustc.wth.circlecircle.R;
import utils.TimeFormat;
import android.content.Context;
import android.content.Intent;
import android.text.TextPaint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.widget.Toast;
import async.ConvPhotoAsyncLoader;
import async.ImageAsyncLoader;
import entity.*;
import buffer.*;

public class ContactListAdapter extends BaseAdapter  implements SectionIndexer{
	Context c;
	private ContactBuffer contactBuffer;
	private List<ContactInfo> contactinfos;
	private LayoutInflater layoutinflater;

	public ContactListAdapter(Context c,List<ContactInfo> contactinfos) {
		this.c = c;
		this.contactinfos=contactinfos;
		this.contactBuffer = new ContactBuffer();
		layoutinflater = LayoutInflater.from(c);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return contactinfos.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return contactinfos.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return contactinfos.get(position).getId();
	}
	
	/**
	 * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
	 */

	
	
	/**
	 * notifyDataSetChanged，调用getview，显示改变后contactinfos的内容
	 * DATE：2014-3-8
	 * Author：王东东
	 */
	public void updateListView(List<ContactInfo> list){
		this.contactinfos = list;
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ConversationHolder holder;
		final ContactInfo mContent = contactinfos.get(position);
		if (convertView == null) {
			convertView = layoutinflater.inflate(R.layout.contact_line,
					null);
			holder = new ConversationHolder();
			//获取三个TextView
			holder.setName((TextView) convertView.findViewById(R.id.contact_name));
			holder.setNum((TextView) convertView.findViewById(R.id.contact_num));
			holder.setImg((TextView) convertView.findViewById(R.id.image_textview));
			holder.setCatalog((TextView) convertView.findViewById(R.id.catalog));

			convertView.setTag(holder);
		} else {
			holder = (ConversationHolder) convertView.getTag();
		}
		
		int section = getSectionForPosition(position);
		
		//如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
		if(position == getPositionForSection(section)){
			holder.getCatalog().setVisibility(View.VISIBLE);
			holder.getCatalog().setText(mContent.getSortLetters());
		}else{
			holder.getCatalog().setVisibility(View.GONE);
		}	
		
		
		ContactInfo cti = contactinfos.get(position).getCti();

		holder.getName().setText(contactinfos.get(position).getName());
		
		String a=Integer.toString(contactinfos.get(position).getId());

		
		holder.getImg().setText(null);
		
		holder.getImg().setBackgroundResource(R.drawable.ic_contact_picture);
		
		
		

		holder.setPosition(position);
		
		//异步加载头像
		new ImageAsyncLoader(c, holder, position, 
				contactinfos.get(position), contactBuffer).execute();


		return convertView;
	}
	
	
	/**
	 * 根据ListView的当前位置获取分类的首字母的Char ascii值
	 */
	public int getSectionForPosition(int position) {
		return contactinfos.get(position).getSortLetters().charAt(0);
	}

	/**
	 * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
	 */
	public int getPositionForSection(int section) {
		for (int i = 0; i < getCount(); i++) {
			String sortStr = contactinfos.get(i).getSortLetters();
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}
		
		return -1;
	}
	
	/**
	 * 提取英文的首字母，非英文字母用#代替。
	 * 
	 * @param str
	 * @return
	 */
	private String getAlpha(String str) {
		String  sortStr = str.trim().substring(0, 1).toUpperCase();
		// 正则表达式，判断首字母是否是英文字母
		if (sortStr.matches("[A-Z]")) {
			return sortStr;
		} else {
			return "#";
		}
	}

	@Override
	public Object[] getSections() {
		return null;
	}

}
