package ustc.wth.circlecircle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import entity.ContactInfo;
import service.ContactService;
import utils.CharacterParser;
import utils.ClearEditText;
import utils.PinyinComparator;
import utils.SideBar;
import utils.SideBar.OnTouchingLetterChangedListener;
import adapter.ContactListAdapter;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.support.v4.app.ListFragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;


public class FragmentContact extends ListFragment implements OnItemLongClickListener, OnItemClickListener {
	private List<ContactInfo> contact_infos;
	private ContactService contact;
	private ClearEditText mClearEditText;
	private ContactListAdapter adapter;
	private SideBar sideBar;
	private TextView dialog;
	private ListView lv;
	private PopupWindow pw;
	private ImageButton conadd_imgbut;
	/**
	 * 根据拼音来排列ListView里面的数据类
	 */
	private CharacterParser characterParser;
	private PinyinComparator pinyinComparator;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_contact, container, false);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initViews();
	}
	
	
	
	/**
	 *listfragment中的监控事件好像都要在onResume里面添加，具体原因也不知道是为什么
	 *其实也好，onResume()的时候会方便 
	 */
	 public void onResume()   
	    {  
	        super.onResume();  
	        
	       
	        characterParser = CharacterParser.getInstance();   //字符和拼音类
			pinyinComparator = new PinyinComparator();
			sideBar = (SideBar) getActivity().findViewById(R.id.sidrbar);   //自定义的侧边栏
			dialog = (TextView) getActivity().findViewById(R.id.dialog);    //单击侧边栏后显示的内容
			sideBar.setTextView(dialog);
			conadd_imgbut= (ImageButton) getActivity().findViewById(R.id.conadd_imgbut);
			getListView().setOnItemLongClickListener(this);
			
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
			
	        
			//获取默认list，添加点击事件
			lv = getListView();   
			lv.setOnItemClickListener(this);
//			lv.setOnItemClickListener(new OnItemClickListener() {
//				@Override
//				public void onItemClick(AdapterView<?> parent, View view,
//						int position, long id) {
//					//这里要利用adapter.getItem(position)来获取当前position所对应的对象
//					//Toast.makeText(getActivity(), ((ContactInfo)adapter.getItem(position)).getName(), Toast.LENGTH_SHORT).show();
//					Intent intent=new Intent();
//					intent.setClass(getActivity(), ContactInfoActivity.class);
//					startActivity(intent);	
//				}
//			});
			
			
			contact = new ContactService(this.getActivity());
			contact_infos = contact.getContactInfo();
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
					filterData(s.toString());
					
				}
				
			});
	        
	    }  
	 
	 
	 
	 
//	 //单击item跳转查看页面
//	  class MyOnItemClickListener implements OnItemClickListener   
//	    {  
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
//				// TODO Auto-generated method stub
//				//Toast.makeText(getActivity(), ((ContactInfo)adapter.getItem(position)).getName(), Toast.LENGTH_SHORT).show();
//				Intent intent=new Intent();
//				intent.setClass(getActivity(), ContactInfoActivity.class);
//				startActivity(intent);	
//				
//			}  
//	    }  

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
	private void filterData(String filterStr){
		//新建一个SortModel类型的List
		List<ContactInfo> filterDateList = new ArrayList<ContactInfo>();

		//判断EditText中是否为空
		if(TextUtils.isEmpty(filterStr))
		{
			filterDateList = contact_infos;
		}
		else
		{
			filterDateList.clear();
			for(ContactInfo contact_info : contact_infos)
			{
				String name = contact_info.getName();
				String phone=contact_info.getPhone();
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
		
		// 根据a-z进行排序
		//Collections.sort(filterDateList, pinyinComparator);
		adapter.updateListView(filterDateList);
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
            
			delContact(ci.getName());
			
			contact_infos.remove(position);
			
			adapter.notifyDataSetChanged();
			
			getListView().invalidate();
		}
		
	}

	
	//删除联系人
	private void delContact(String name) {
		ContentResolver resolver = this.getActivity().getContentResolver();

		Cursor cursor = resolver.query(Data.CONTENT_URI,new String[] { Data.RAW_CONTACT_ID },

		ContactsContract.Contacts.DISPLAY_NAME + "=?",new String[] { name }, null);

		ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

		if (cursor.moveToFirst()) {
		do {
		long Id = cursor.getLong(cursor.getColumnIndex(Data.RAW_CONTACT_ID));

		ops.add(ContentProviderOperation.newDelete(
		ContentUris.withAppendedId(RawContacts.CONTENT_URI,Id)).build());
		try {
			resolver.applyBatch(ContactsContract.AUTHORITY, ops);
		} 
		catch (Exception e){}
		} while (cursor.moveToNext());
		cursor.close();
		}
		}
	
	
	
	}

