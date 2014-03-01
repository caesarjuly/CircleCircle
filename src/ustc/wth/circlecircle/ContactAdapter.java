package ustc.wth.circlecircle;

import java.util.List;

import entity.Contactinfo;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

/**
 * 鑱旂郴浜哄垪琛ㄩ?閰嶅櫒銆?
 * 
 * @author guolin
 */
public class ContactAdapter extends ArrayAdapter<Contactinfo> {

	/**
	 * 需要渲染的item布局文件
	 */
	private int resource;

	/**
	 *  字母表分组工具 
	 */
	private SectionIndexer mIndexer;

	public ContactAdapter(Context context, int textViewResourceId, List<Contactinfo> objects) {
		super(context, textViewResourceId, objects);
		resource = textViewResourceId;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Contactinfo contact = getItem(position);
		LinearLayout layout = null;
		if (convertView == null) {
			layout = (LinearLayout) LayoutInflater.from(getContext()).inflate(resource, null);
		} else {
			layout = (LinearLayout) convertView;
		}
		TextView name = (TextView) layout.findViewById(R.id.contact_name);
		TextView num = (TextView) layout.findViewById(R.id.contact_num);
		//LinearLayout sortKeyLayout = (LinearLayout) layout.findViewById(R.id.sort_key_layout);
		//TextView sortKey = (TextView) layout.findViewById(R.id.sort_key);
		name.setText(contact.getName());
		num.setText(contact.getPhoneNumber());;
		int section = mIndexer.getSectionForPosition(position);
		
//		if (position == mIndexer.getPositionForSection(section)) {
//			sortKey.setText(contact.getSortKey());
//			sortKeyLayout.setVisibility(View.VISIBLE);
//		} else {
//			sortKeyLayout.setVisibility(View.GONE);
//		}
		return layout;
	}

	/**
	 * 缁欏綋鍓嶉?閰嶅櫒浼犲叆涓?釜鍒嗙粍宸ュ叿銆?
	 * 
	 * @param indexer
	 */
	public void setIndexer(SectionIndexer indexer) {
		mIndexer = indexer;
	}
	

	

}
