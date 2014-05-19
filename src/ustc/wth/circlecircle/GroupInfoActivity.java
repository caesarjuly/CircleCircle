package ustc.wth.circlecircle;



import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;








import ustc.wth.circlecircle.FragmentContact.ConDelListener;
import ustc.wth.circlecircle.FragmentContact.ConEditListener;
import ustc.wth.circlecircle.FragmentContact.MyButtonClickListener;
import entity.ContactInfo;
import entity.GroupInfo;
import adapter.ContactListAdapter;
import adapter.GroupAdapter;
import adapter.GroupsListAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.Groups;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;


public class GroupInfoActivity extends Activity {
	
	private static final int CONTEXT_MENU_ITEM_ADD_GROUP = Menu.FIRST;
	private static final int CONTEXT_MENU_ITEM_EDIT_GROUP = Menu.FIRST+1;
	private static final int CONTEXT_MENU_ITEM_DELTE_GROUP = Menu.FIRST+2;

	private PopupWindow popupWindow;
	private PopupWindow pw;
	private GroupsListAdapter groupAdapter;
	private List<GroupInfo> groupinfos;
	private HashMap<String, String> contactgroup;
	private ListView groupslist;
	private Activity groupactivity;
	private ImageButton groupadd_imgbut;
	
	@SuppressWarnings("deprecation")
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.groups_list);

        groupadd_imgbut=(ImageButton)findViewById(R.id.groupadd_imgbut);
        
        MyButtonClickListener onclickListener = new MyButtonClickListener();   
        groupadd_imgbut.setOnClickListener(onclickListener);  
        
        groupinfos = new ArrayList<GroupInfo>();

        contactgroup=new HashMap<String,String>();

        groupactivity=GroupInfoActivity.this;
        
        String[] RAW_PROJECTION = new String[] { ContactsContract.Groups._ID, ContactsContract.Groups.TITLE };  
        String RAW_CONTACTS_WHERE = ContactsContract.Groups.DELETED + " = ? ";  
        Cursor groupcursor = getContentResolver().query(ContactsContract.Groups.CONTENT_URI, RAW_PROJECTION,  
                RAW_CONTACTS_WHERE, new String[] { "" + 0 }, null);  
        if (groupcursor != null) {
			while (groupcursor.moveToNext()) {
					String title = groupcursor.getString(groupcursor.getColumnIndex("title"));
					String id=groupcursor.getString(groupcursor.getColumnIndex("_id"));
					GroupInfo groupentity=new GroupInfo();
					groupentity.setGroupid(id);
					groupentity.setName(title);
					groupinfos.add(groupentity);
					contactgroup.put(title,id);
				}
			}
        groupcursor.close();

        groupAdapter=new GroupsListAdapter(groupactivity,this.getApplicationContext(), groupinfos);       

        groupslist=(ListView)findViewById(R.id.groupsListView);
        
        groupslist.setOnItemLongClickListener(new OnItemLongClickListener() {  
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {  
            	LinearLayout pv = (LinearLayout) LayoutInflater.from(
            			GroupInfoActivity.this).inflate(R.layout.pop_up_contact, null);

        		pw = new PopupWindow(GroupInfoActivity.this);
        		pw.setContentView(pv);
        		Drawable dw = getResources().getDrawable(R.drawable.qzone_bg_copy);
        		pw.setBackgroundDrawable(dw);

        		TextView conEdit = (TextView) pv.findViewById(R.id.contact_edit);
        		TextView conDel = (TextView) pv.findViewById(R.id.contact_delete);
        		ImageView ivLine = (ImageView) pv.findViewById(R.id.line);
        				
        		GroupInfo gpi = groupinfos.get(position);
        		// popwindow的长和宽的，必须要设置的，不然无法显示的
        		pw.setWidth(400);
        		pw.setHeight(150);
        		pw.setOutsideTouchable(true);
        		pw.setFocusable(true);

        		View line = view;
                pw.showAsDropDown(line, line.getWidth()/2-200, -line.getHeight()-75);
                conEdit.setOnClickListener(new ConEditListener(gpi.getGroupid(), gpi));
                conDel.setOnClickListener(new ConDelListener(gpi.getGroupid(), gpi, position));
        		return false;
            }  
        });  
        

        TextView textview=(TextView)findViewById(R.id.message_title);
        groupslist.setAdapter(groupAdapter);

//        getContentResolver().registerContentObserver(Uri.parse(Uris.SMS_URI_ALL), true, new SmsObserver(new Handler()));
        }
	
	protected void onReusme()
	{
		super.onResume();
		String[] RAW_PROJECTION = new String[] { ContactsContract.Groups._ID, ContactsContract.Groups.TITLE };  
        String RAW_CONTACTS_WHERE = ContactsContract.Groups.DELETED + " = ? ";  
        Cursor groupcursor = getContentResolver().query(ContactsContract.Groups.CONTENT_URI, RAW_PROJECTION,  
                RAW_CONTACTS_WHERE, new String[] { "" + 0 }, null);  
        if (groupcursor != null) {
			while (groupcursor.moveToNext()) {
					String title = groupcursor.getString(groupcursor.getColumnIndex("title"));
					String id=groupcursor.getString(groupcursor.getColumnIndex("_id"));
					GroupInfo groupentity=new GroupInfo();
					groupentity.setGroupid(id);
					groupentity.setName(title);
					groupinfos.add(groupentity);
					contactgroup.put(title,id);
				}
			}
        groupcursor.close();
		
		
	}
	
	  class MyButtonClickListener implements OnClickListener   
	    {  
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub		

				AddgroupDialog();  //true为调用编辑
				
				groupAdapter.notifyDataSetChanged();
			}	
	    }  
	
	class ConEditListener implements OnClickListener{	
		private String id;
		private GroupInfo gri;
		ConEditListener(String id, GroupInfo gri){
			this.gri = gri;
			this.id = id;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub		
			pw.dismiss();     
			String gid = gri.getGroupid();  
			showDialog(true,gid,gri);  //true为调用编辑
		}		
	}
	
	class ConDelListener implements OnClickListener{
		private String id="";
		private GroupInfo gri;
		private int position;
		ConDelListener(String id, GroupInfo gri,int position){
			this.gri = gri;
			this.id = id;
			this.position=position;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			pw.dismiss();     
			String gid = gri.getGroupid();  
	        getContentResolver().delete(  
	                Uri.parse(Groups.CONTENT_URI + "?" + ContactsContract.CALLER_IS_SYNCADAPTER + "=true"),  
	                Groups._ID + "=" + gid, null);  
			groupinfos.remove(position);
			groupAdapter.notifyDataSetChanged();
			groupslist.invalidate();
		}	
	}
	
	private void AddgroupDialog() {
		// 弹出Dialog,供用户输入分组的名称，点击确定后保存分组
		AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
		builder2.setTitle("请输入分组名称：");
		final EditText addGroupName = new EditText(this);
		addGroupName.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));
		builder2.setView(addGroupName);
		builder2.setPositiveButton(R.string.save,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// 保存分组，并刷新页面显示刚添加的分组
						String newGroupName = addGroupName.getText().toString();
						if (!TextUtils.isEmpty(newGroupName)) {
								saveNewGroup(newGroupName);
							dialog.dismiss();
						} else {// 分组名称为空
							Toast.makeText(groupactivity,
									"分组名是空",
									Toast.LENGTH_SHORT).show();
						}
					}
				});
		builder2.setNegativeButton(R.string.canel,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		// 显示Dialog
		builder2.create().show();
	}
	
	private void saveNewGroup(String newGroupName) {
		ContentValues values = new ContentValues();
		values.put(Groups.TITLE, newGroupName);
		getContentResolver().insert(Groups.CONTENT_URI, values);
		// 刷新
		groupAdapter.notifyDataSetChanged();
	}
	
	private void showDialog(final boolean isEdit, final String gid,final GroupInfo gri) {
		// 弹出Dialog,供用户输入分组的名称，点击确定后保存分组
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("请输入分组名称：");
		final EditText etGroupName = new EditText(this);
		etGroupName.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));
		builder.setView(etGroupName);
		builder.setPositiveButton(R.string.save,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// 保存分组，并刷新页面显示刚添加的分组
						String newGroupName = etGroupName.getText().toString();
						if (!TextUtils.isEmpty(newGroupName)) {
							if (isEdit) {// 修改分组名称
								updateGroupName(newGroupName,gid);
								gri.setName(newGroupName);
								groupAdapter.notifyDataSetChanged();
								groupslist.invalidate();
								Toast.makeText(groupactivity,
										"修改成功",
										Toast.LENGTH_SHORT).show();
							} else {// 新建分组
								//saveNewGroup(newGroupName);
							}
							dialog.dismiss();
						} else {// 分组名称为空
							Toast.makeText(groupactivity,
									"分组名是空",
									Toast.LENGTH_SHORT).show();
						}
					}
				});
		builder.setNegativeButton(R.string.canel,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		// 显示Dialog
		builder.create().show();
	}
	
	private void updateGroupName(String newGroupName,String gid) {
		ContentValues values = new ContentValues();
		values.put(Groups.TITLE, newGroupName);
		getContentResolver().update(Groups.CONTENT_URI, 
			values, 
			ContactsContract.Groups._ID  + " = " + gid, 
			null);
		// 刷新
		groupAdapter.notifyDataSetChanged();
	}
}
