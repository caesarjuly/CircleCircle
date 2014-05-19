package adapter;

import java.util.List;

import entity.GroupInfo;
import ustc.wth.circlecircle.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class EditGroupadapter extends BaseAdapter {  
	  
    private Context mContext;  
    private List<GroupInfo> mList;  
  
    public EditGroupadapter(Context context, List<GroupInfo> list) {  
        this.mContext = context;  
        this.mList = list;  
    }  
  
    @Override  
    public int getCount() {  
        // TODO Auto-generated method stub  
        return mList.size();  
    }  
  
    @Override  
    public Object getItem(int position) {  
        // TODO Auto-generated method stub  
        return mList.get(position);  
    }  
  
    @Override  
    public long getItemId(int index) {  
        // TODO Auto-generated method stub  
        return index;  
    }  
  


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub  
        LayoutInflater _LayoutInflater = LayoutInflater.from(mContext);  
        convertView = _LayoutInflater.inflate(R.layout.contactgroup_item, null);  
        if (convertView != null) {  
            TextView TextView1 = (TextView) convertView  
                    .findViewById(R.id.textViewName);  
//            TextView TextView2 = (TextView) convertView  
//                    .findViewById(R.id.textViewNumber);  
            TextView1.setText(mList.get(position).getName());  
//            TextView2.setText(mList.get(position).getName());  
        }  
        return convertView;  
	}  
  
}