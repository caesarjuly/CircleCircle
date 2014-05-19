package adapter;

import holder.ConversationHolder;
import holder.GroupsHolder;

import java.util.Iterator;
import java.util.List;

import entity.ContactInfo;
import entity.ConversationInfo;
import entity.GroupInfo;
import service.ContactService;
import ustc.wth.circlecircle.R;
import utils.TimeFormat;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GroupsListAdapter extends BaseAdapter {  
	  
	private List<GroupInfo> groupinfos;
	private List<ContactInfo> contactinfos;
	Context c;
	private LayoutInflater layoutinflater;
    private List<String> list;  
    private ContactService contact;
    private Activity activity;
  
	
	public GroupsListAdapter(Activity activity,Context c, List<GroupInfo> groupinfos) {
		this.c = c;
		this.groupinfos = groupinfos;
		this.activity=activity;
		layoutinflater = LayoutInflater.from(c);
	}
    

    public int getCount() {  
        return groupinfos.size();  
    }  
    public Object getItem(int position) {  
  
        return groupinfos.get(position); 
    }  
    public long getItemId(int position) {  
        return Integer.parseInt(groupinfos.get(position).getGroupid());  
    }  
  
    public View getView(int position, View convertView, ViewGroup viewGroup) {       
    	GroupsHolder holder;  
    	GroupInfo grovInfo = groupinfos.get(position);
        if (convertView==null) {  
            convertView=layoutinflater.inflate(R.layout.groups_item, null);  
            holder=new GroupsHolder();  
            holder.setGroupsname((TextView) convertView.findViewById(R.id.groupname));  
            holder.setGroupmembercount((TextView) convertView.findViewById(R.id.groupmemberCount));
            holder.setGroupimg((TextView) convertView.findViewById(R.id.groupimage));
            convertView.setTag(holder);  
        }  
        else{  
            holder=(GroupsHolder) convertView.getTag();  
        }  
        
        if(contactinfos==null)
        {
        	contact = new ContactService(activity);
            contactinfos=contact.getContactInfo();
        }
        
        Iterator it = contactinfos.iterator();
        ContactInfo contactinfoss=new ContactInfo();
        String groupid2222="";
        int groupmembercount=0;
        while (it.hasNext()) {
        	contactinfoss= (ContactInfo) it.next();
        	if(contactinfoss.getGroupid().equals(grovInfo.getGroupid()))
        	groupmembercount++;
        }
        holder.getGroupmembercount().setText(Integer.toString(groupmembercount));
		holder.getGroupsname().setText(grovInfo.getName());
		holder.getGroupimg()
		.setBackgroundResource(R.drawable.ic_contacts_picture);
		
		holder.setPosition(position);
          
        return convertView;  
    }  
  
    static class ViewHolder {  
        TextView groupItem;  
    }  
  
}  