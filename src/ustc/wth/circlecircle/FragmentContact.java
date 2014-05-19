package ustc.wth.circlecircle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import android.util.Log;
import android.view.Window; 
import entity.ContactInfo;
import service.ContactService;
import utils.CharacterParser;
import utils.ClearEditText;
import utils.PinyinComparator;
import utils.SideBar;
import utils.SideBar.OnTouchingLetterChangedListener;
import adapter.ContactListAdapter;
import adapter.GroupAdapter;
import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.Groups;
import android.provider.ContactsContract.RawContacts;
import android.provider.ContactsContract.CommonDataKinds.GroupMembership;
import android.support.v4.app.ListFragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;


public class FragmentContact extends ListFragment implements OnItemLongClickListener, OnItemClickListener {
	private static final int OPTIONS_MENU_ITEM_ADD = Menu.FIRST;
	private static final int OPTIONS_MENU_ITEM_GROUPS = Menu.FIRST + 1;
	private List<ContactInfo> contact_infos;
	private HashMap<String, String> contactgroup;
	private ContactService contact;

	private ClearEditText mClearEditText;
	private ContactListAdapter adapter;
	private GroupAdapter groupAdapter;
	private SideBar sideBar;
	private TextView dialog;
	private ListView lv;
	private PopupWindow pw;
	private ImageButton conadd_imgbut;
	private PopupWindow popupWindow;
	private Context content;
	private ListView lv_group;  
	private String staticgroupid="999";
    private View view;  
    private static FragmentContact instance;
	private TextView tvtitle;  
	/**
	 * 根据拼音来排列ListView里面的数据类
	 */
	private CharacterParser characterParser;
	private PinyinComparator pinyinComparator;
	private List<String> groupinfos;
	
	private Activity group_activity;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_contact, container, false);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		contactgroup=new HashMap<String,String>();
		initViews();	
	}
	
	/**
	 *listfragment中的监控事件好像都要在onResume里面添加，具体原因也不知道是为什么
	 *其实也好，onResume()的时候会方便 
	 */
	 public void onResume()   
	    {  
	        super.onResume();  
	        
	        tvtitle = (TextView) getActivity().findViewById(R.id.tvtitle); 
	        
	        tvtitle.setOnClickListener(new View.OnClickListener() {  
	        	  
	            @Override  
	            public void onClick(View v) {  
	                showWindow(v);  
	            }  
	        });  
	        
	        characterParser = CharacterParser.getInstance();   //字符和拼音类
			pinyinComparator = new PinyinComparator();
			sideBar = (SideBar) getActivity().findViewById(R.id.sidrbar);   //自定义的侧边栏
			dialog = (TextView) getActivity().findViewById(R.id.dialog);    //单击侧边栏后显示的内容
			sideBar.setTextView(dialog);
			conadd_imgbut= (ImageButton) getActivity().findViewById(R.id.conadd_imgbut);
			getListView().setOnItemLongClickListener(this);
			registerForContextMenu(getListView());
			//ImageButton添加监听事件
			MyButtonClickListener onclickListener = new MyButtonClickListener();   
	        conadd_imgbut.setOnClickListener(onclickListener);  
			
			//侧边栏添加监听事件
			sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {
				@Override
				public void onTouchingLetterChanged(String s) {
					//该字母首次出现的位置
					int position = adapter.getPositionForSection(s.charAt(0));
					if(position != -1){
						lv.setSelection(position);
					}
					
				}
			});
			
			lv = getListView();    //获取默认list，添加点击事件
			lv.setOnItemClickListener(this);
			content =this.getActivity();
			group_activity=this.getActivity();
			contact = new ContactService(this.getActivity());
			contact_infos = contact.getContactInfo();              //调用contactservice初始化数据
			Collections.sort(contact_infos, pinyinComparator);     //对数据源按照拼音进行排序
			adapter=new ContactListAdapter(this.getActivity(), contact_infos);
			setListAdapter(adapter);
			mClearEditText=(ClearEditText)getActivity().findViewById(R.id.filter_edit);   //自定义ClearEditText
			//为editatext添加响应事件
			mClearEditText.addTextChangedListener(new TextWatcher(){

				@Override
				public void afterTextChanged(Editable s) {
					// TODO Auto-generated method stub				
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start, int count,
						int after) {
					// TODO Auto-generated method stub			
				}

				@Override
				public void onTextChanged(CharSequence s, int start, int before,
						int count) {
					// TODO Auto-generated method stub
					//根据改变的内容来填充数据源
					filterData(s.toString(),staticgroupid);				
				}				
			});     
	    }   

	 //上下文菜单
	 @Override 
     public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) { 
         // TODO Auto-generated method stub 
         super.onCreateOptionsMenu(menu, inflater); 
         menu.add(0, OPTIONS_MENU_ITEM_ADD, 0, "添加联系人");  
	     menu.add(0, OPTIONS_MENU_ITEM_GROUPS , 0, "管理分组");  
     } 
       
	 public boolean onOptionsItemSelected(MenuItem item) {
			switch (item.getItemId()) {
			case OPTIONS_MENU_ITEM_ADD:
//				final Intent intent = new Intent(Intent.ACTION_INSERT,
//						Contacts.CONTENT_URI);
//				startActivity(intent);
				return true;
			case OPTIONS_MENU_ITEM_GROUPS:// 跳转到显示所有分组页面
				final Intent groupIntent = new Intent(getActivity().getApplicationContext(), GroupInfoActivity.class);
				startActivity(groupIntent);
			default:
				return super.onContextItemSelected(item);
			}
		}

	  //单击添加联系人按钮跳转至新建联系人页面
	  class MyButtonClickListener implements OnClickListener   
	    {  
	        public void onClick(View v)   
	        {       	
	        	Intent intent=new Intent();
				intent.setClass(getActivity(), ContactAddActivity.class);
				startActivity(intent);	        	
	        }  
	    }  
	 

	 //初始化拼音类
	  private void initViews() {
		  
		characterParser = CharacterParser.getInstance();	
		pinyinComparator = new PinyinComparator();

	  }

	
	/**
	 * 根据输入框中的值来过滤数据并更新ListView，目前可以按照拼音和名字来进行搜索，目前不支持全拼
	 * DATE：2014-3-8
	 * Author：王东东
	 */
	private void filterData(String filterStr,String groupid){
		//新建一个SortModel类型的List
		List<ContactInfo> filterDateList = new ArrayList<ContactInfo>();

		//判断EditText中是否为空
		if(groupid.equals("999"))
		{		
			if(TextUtils.isEmpty(filterStr))
			{
				filterDateList.clear();
				filterDateList = contact_infos;
			}
			else
			{
				filterDateList.clear();
				for(ContactInfo contact_info : contact_infos)
				{
					String name = contact_info.getName();
					if(name!=null)
					{
						//返回字符中indexof（string）中字串string在父串中首次出现的位置，从0开始！没有返回-1；方便判断和截取字符串！

						//if(name.indexOf(filterStr.toString()) != -1 || characterParser.getSelling(name).startsWith(filterStr.toString())){
					    if(name.indexOf(filterStr.toString()) != -1 ||  characterParser.getSelling(name).startsWith(filterStr.toString()))
					    {
							//filterDateList中的数据改变

							filterDateList.add(contact_info);
						}
					}
				}
			}		
		}
		else
		{
			filterDateList.clear();
			for(ContactInfo contact_info : contact_infos)
			{
				String name = contact_info.getName();
				String phone=contact_info.getPhone();
				String groupid_info=contact_info.getGroupid();
				if(name!=null&&groupid_info.equals(groupid))
				{
					//返回字符中indexof（string）中字串string在父串中首次出现的位置，从0开始！没有返回-1；方便判断和截取字符串！
					//if(name.indexOf(filterStr.toString()) != -1 || characterParser.getSelling(name).startsWith(filterStr.toString())){
				    if(name.indexOf(filterStr.toString()) != -1 ||  characterParser.getSelling(name).startsWith(filterStr.toString()))
				    {
						//filterDateList中的数据改变

						filterDateList.add(contact_info);
					}
				}
			}		
		}
		
		
		// 根据a-z进行排序
		//Collections.sort(filterDateList, pinyinComparator);
		adapter.updateListView(filterDateList);
		}
	
	//填充分组listview的信息
	private void filtergroupData(String groupid)
	{
		List<ContactInfo> filtergroupList = new ArrayList<ContactInfo>();
		if(groupid.equals("999"))
		{
			filtergroupList.clear();
			filtergroupList = contact_infos;
			Log.d("gorupitems",Integer.toString(filtergroupList.size()));
		}
		else
		{
			filtergroupList.clear();
			for(ContactInfo contact_info : contact_infos)
			{
				String name = contact_info.getName();
				String phone=contact_info.getPhone();
				String group_id=contact_info.getGroupid();
				if(group_id.equals(groupid))
				{
					filtergroupList.add(contact_info);
				}
			}
		}	
		adapter.updateListView(filtergroupList);
		Log.d("adapter", Integer.toString(adapter.getCount()));
		
	}
	
	
	//单击Item跳转至查看页面
	public void onItemClick(AdapterView<?> parent, View view,int position, long id) {	
		ContactInfo ci = (ContactInfo)adapter.getItem(position);
		Bundle bundle = new Bundle();
		Intent intent=new Intent();
		intent.setClass(getActivity().getApplicationContext(),ContactInfoActivity.class);
		bundle.putSerializable("ContactInfo1", ci);
        intent.putExtras(bundle);
		startActivity(intent);
		 
	 }	
	
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		LinearLayout pv = (LinearLayout) LayoutInflater.from(
				getActivity()).inflate(R.layout.pop_up_contact, null);

		pw = new PopupWindow(getActivity());
		pw.setContentView(pv);
		Drawable dw = getResources().getDrawable(R.drawable.qzone_bg_copy);
		pw.setBackgroundDrawable(dw);

		TextView conEdit = (TextView) pv.findViewById(R.id.contact_edit);
		TextView conDel = (TextView) pv.findViewById(R.id.contact_delete);
		ImageView ivLine = (ImageView) pv.findViewById(R.id.line);
				
		ContactInfo ci = contact_infos.get(arg2);
		// popwindow的长和宽的，必须要设置的，不然无法显示的
		pw.setWidth(400);
		pw.setHeight(150);
		pw.setOutsideTouchable(true);
		pw.setFocusable(true);

		View line = arg1;
        pw.showAsDropDown(line, line.getWidth()/2-200, -line.getHeight()-75);
        conEdit.setOnClickListener(new ConEditListener(ci.getId(), ci));
        conDel.setOnClickListener(new ConDelListener(ci.getId(), ci, arg2));
		return false;
	}

	class ConEditListener implements OnClickListener{	
		private int id;
		private ContactInfo ci;
		ConEditListener(int id, ContactInfo ci){
			this.ci = ci;
			this.id = id;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub		
			Bundle bundle = new Bundle();
			Intent intent=new Intent();
		    intent.setClass(getActivity().getApplicationContext(),ContactEditActivity.class);
		    bundle.putSerializable("ContactInfo1", ci);
            intent.putExtras(bundle);
            pw.dismiss();
            startActivity(intent);
		}		
	}
	
	class ConDelListener implements OnClickListener{
		private int id;
		private ContactInfo ci;
		private int position;
		ConDelListener(int id, ContactInfo ci,int position){
			this.ci = ci;
			this.id = id;
			this.position=position;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			pw.dismiss();     
			delContact(getActivity(),ci.getName());
			contact_infos.remove(position);
			adapter.notifyDataSetChanged();
			getListView().invalidate();
		}	
	}
	
	
	private void showWindow(View parent) {  
		  
        if (popupWindow == null) {  
            LayoutInflater layoutInflater = (LayoutInflater) this.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
            WindowManager mWm = (WindowManager)this.getActivity().getSystemService(Context.WINDOW_SERVICE);   
            view = layoutInflater.inflate(R.layout.group_list, null);  
  
            String[] RAW_PROJECTION = new String[] { ContactsContract.Groups._ID, ContactsContract.Groups.TITLE, };  
            String RAW_CONTACTS_WHERE = ContactsContract.Groups.DELETED + " = ? ";  
            Cursor groupcursor = this.getActivity().getContentResolver().query(ContactsContract.Groups.CONTENT_URI, RAW_PROJECTION,  
                    RAW_CONTACTS_WHERE, new String[] { "" + 0 }, null);  
            
            groupinfos = new ArrayList<String>();
            
            if (groupcursor != null) {
    			while (groupcursor.moveToNext()) {
    					//利用哈希map来保存联系人分组及对应的组号
    					String title = groupcursor.getString(groupcursor.getColumnIndex("title"));
    					String id=groupcursor.getString(groupcursor.getColumnIndex("_id"));
    					groupinfos.add(title);
    					contactgroup.put(title,id);
    				}
    			}
    			groupcursor.close();
    			groupinfos.add("未分组");
    			contactgroup.put("未分组","999");
            lv_group = (ListView) view.findViewById(R.id.lvGroup);  
            groupAdapter = new GroupAdapter(this.getActivity(), groupinfos);  
            lv_group.setAdapter(groupAdapter);  
                
            // 创建一个PopuWidow对象  
            popupWindow = new PopupWindow(view, 350, 550);  
        }  
         
        popupWindow.setFocusable(true);  // 使其聚集
        popupWindow.setOutsideTouchable(true);  // 设置允许在外点击消失  
  
        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景  
        popupWindow.setBackgroundDrawable(new BitmapDrawable());  
        WindowManager windowManager = (WindowManager) this.getActivity().getSystemService(Context.WINDOW_SERVICE);  
        
        // 显示的位置为:屏幕的宽度的一半-PopupWindow的高度的一半  
        //        int xPos = windowManager.getDefaultDisplay().getWidth() / 2  
       
        //                - popupWindow.getWidth() / 2;
        int xPos =0;
        Log.i("coder", "xPos:" + xPos);  
        popupWindow.showAsDropDown(parent, xPos, 0);  
        lv_group.setOnItemClickListener(new OnItemClickListener() {  
            @Override  
            
            
            public void onItemClick(AdapterView<?> parent, View view,  
                    int position, long id) {  
            	
            	//获取当前Item中存储的值，利用hashmap获取当前对应value
            	String ss=(String)groupAdapter.getItem(position);
            	String ssid=contactgroup.get(ss);
            	tvtitle.setText(ss);
            	//若果ssid=999，为默认未分组
            	if(ssid.equals("999")){               
            		staticgroupid=ssid;
            		filtergroupData("999");
            	}
            	else
            	{
            		staticgroupid=ssid;
    				filtergroupData(ssid);
            	}
				
            	if (popupWindow != null) {  
                    popupWindow.dismiss();  
                }  
            }  
        });  
    }  
	
	public static FragmentContact getInstance() {
        // TODO Auto-generated method stub
        return instance;
	}
	
	private void delContact(Context context, String name) {

		Cursor cursor = getActivity().getContentResolver().query(Data.CONTENT_URI,new String[] { Data.RAW_CONTACT_ID },

		ContactsContract.Contacts.DISPLAY_NAME + "=?",new String[] { name }, null);

		ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
		ArrayList<ContentProviderOperation> ops1 = new ArrayList<ContentProviderOperation>();
		if (cursor.moveToFirst()) {
		do {
		long Id = cursor.getLong(cursor.getColumnIndex(Data.RAW_CONTACT_ID));
		ops.add(ContentProviderOperation.newDelete(
		ContentUris.withAppendedId(RawContacts.CONTENT_URI,Id)).build());
		ops1.add(ContentProviderOperation.newDelete(
				ContentUris.withAppendedId(Data.CONTENT_URI,Id)).build());
		try {
		getActivity().getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
		getActivity().getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops1);
		} 
		catch (Exception e){}
		} while (cursor.moveToNext());
		
		cursor.close();

		
		}
		}
	//CR.delete(ContactsContract.RawContacts.CONTENT_URI,ContactsContract.RawContacts_id + "=" + delRawId);
	
	
	ArrayList<ContentProviderOperation> ops =
	          new ArrayList<ContentProviderOperation>();
	
	}

