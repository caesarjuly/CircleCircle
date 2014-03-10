package ustc.wth.circlecircle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;





import entity.ContactInfo;
import service.ContactService;
import ustc.wth.circlecircle.SideBar.OnTouchingLetterChangedListener;
import adapter.ContactListAdapter;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;


public class FragmentContact extends ListFragment {
	private List<ContactInfo> contact_infos;
	private ContactService contact;
	private ClearEditText mClearEditText;
	private EditText editatext;
	private ContactListAdapter adapter;
	private TextView textview;
	private SideBar sideBar;
	private TextView dialog;
	private ListView lv;
	
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
	        
			lv = getListView();   //获取默认list，添加点击事件
			MyOnItemClickListener clickListener = new MyOnItemClickListener();   
			
			//lv.setOnItemLongClickListener(listener);
			lv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					//这里要利用adapter.getItem(position)来获取当前position所对应的对象
					Toast.makeText(getActivity(), ((ContactInfo)adapter.getItem(position)).getName(), Toast.LENGTH_SHORT).show();
				}
			});
				
	        mClearEditText=(ClearEditText)getActivity().findViewById(R.id.filter_edit);   //自定义ClearEditText

			contact = new ContactService(this.getActivity());
			contact_infos = contact.getContactInfo();
			
			Collections.sort(contact_infos, pinyinComparator);     //对数据源按照拼音进行排序
			adapter=new ContactListAdapter(this.getActivity(), contact_infos);
			setListAdapter(adapter);
			
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
	        
//	        contact = new ContactService(this.getActivity());
//	        
//			contact_infos = contact.getContactInfo();
//			
//			setListAdapter(new ContactListAdapter(this.getActivity(), contact_infos));
//	     
//	        button = (Button) getActivity().findViewById(R.id.button32);  
//	        
//	        button.setText("新增");
//
//	        MyButtonClickListener clickListener = new MyButtonClickListener();   
//	        
//	        button.setOnClickListener(clickListener);  

	    }  
	 
	  class MyOnItemClickListener implements OnItemClickListener   
	    {  
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Toast.makeText(getActivity(), ((ContactInfo)adapter.getItem(position)).getName(), Toast.LENGTH_SHORT).show();
			}  
	    }  

	 
	 
	 
	
	 //初始化拼音类
	private void initViews() {
		characterParser = CharacterParser.getInstance();
		
		pinyinComparator = new PinyinComparator();

	}
	
	
	/**
	 * 为ListView填充数据
	 * @param date
	 * @return
	 */
	private List<SortModel> filledData(String [] date){
		List<SortModel> mSortList = new ArrayList<SortModel>();
		
		for(int i=0; i<date.length; i++){
			SortModel sortModel = new SortModel();
			sortModel.setName(date[i]);
			//汉字转换成拼音
			String pinyin = characterParser.getSelling(date[i]);
			String sortString = pinyin.substring(0, 1).toUpperCase();
			
			// 正则表达式，判断首字母是否是英文字母
			if(sortString.matches("[A-Z]")){
				sortModel.setSortLetters(sortString.toUpperCase());
			}else{
				sortModel.setSortLetters("#");
			}
			
			mSortList.add(sortModel);
		}
		return mSortList;
		
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

	
	
	
	}

