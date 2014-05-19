package ustc.wth.circlecircle;

import java.util.ArrayList;
import java.util.List;

import entity.CallInfo;
import entity.ConversationInfo;
import service.CallService;
import ustc.wth.circlecircle.FragmentConversation.ConDelListener;
import ustc.wth.circlecircle.FragmentConversation.TelCallListener;
import utils.CharacterParser;
import utils.ClearEditText;
import utils.ConvNameFormat;
import utils.Uris;
import adapter.CallListAdapter;
import adapter.ConversationListAdapter;
import android.content.Intent;
import android.database.ContentObserver;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.CallLog;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.AdapterView.OnItemLongClickListener;
import async.ConvNameAsyncLoader;

public class FragmentHome extends ListFragment implements
OnItemLongClickListener {
	private CallService cs;
	private List<CallInfo> callList;
	private CallListAdapter callListAdapter;
	PopupWindow pw;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_home, container,
				false);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		cs = new CallService(this.getActivity());
		callList = cs.getCallLog();
		callListAdapter = new CallListAdapter(this.getActivity(),
				callList);
		setListAdapter(callListAdapter);
		//getActivity().getContentResolver().registerContentObserver(CallLog.Calls.CONTENT_URI, true, new CallObserver(new Handler()));
		
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
				LinearLayout pv = (LinearLayout) LayoutInflater.from(getActivity())
						.inflate(R.layout.pop_up_conversation, null);

				pw = new PopupWindow(getActivity());
				pw.setContentView(pv);
				Drawable dw = getResources().getDrawable(R.drawable.qzone_bg_copy);
				pw.setBackgroundDrawable(dw);

				TextView tvCall = (TextView) pv.findViewById(R.id.conversation_call);
				TextView tvDel = (TextView) pv.findViewById(R.id.conversation_delete);
				ImageView ivLine = (ImageView) pv.findViewById(R.id.line);

				CallInfo callInfo = callList.get(arg2);
				tvCall.setVisibility(View.GONE);
				ivLine.setVisibility(View.GONE);


				// popwindow的长和宽的，必须要设置的，不然无法显示的
				pw.setWidth(400);
				pw.setHeight(150);

				pw.setOutsideTouchable(true);
				pw.setFocusable(true);

				View line = arg1;
				pw.showAsDropDown(line, line.getWidth() / 2 - 200,
						-line.getHeight() - 75);

				tvDel.setOnClickListener(new CallDelListener(callInfo));
		return false;
	}	
	
	class CallDelListener implements OnClickListener {
		private CallInfo ci;

		CallDelListener(CallInfo ci) {
			this.ci = ci;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			pw.dismiss();
			cs.deleteCall(ci.getPhone());
			callListAdapter.removeCall(ci);
			callListAdapter.notifyDataSetChanged();
			pw = null;
		}

	}
	
	private final class CallObserver extends ContentObserver{  
        public CallObserver(Handler handler) {  
            super(handler);  
        }  
        @Override  
        public void onChange(boolean selfChange) {  
        	callList = cs.getCallLog();
        	callListAdapter.updateListView(callList);
            }  
        } 
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		getListView().setOnItemLongClickListener(this);
		ClearEditText mClearEditText = (ClearEditText) getActivity().findViewById(
				R.id.message_filter); // 自定义ClearEditText
		// 为editatext添加响应事件
		mClearEditText.addTextChangedListener(new TextWatcher() {

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
				// 根据改变的内容来填充数据源
				filterData(s.toString());

			}

		});

	}

	/**
	 * 根据输入框中的值来过滤数据并更新ListView，目前可以按照拼音和名字来进行搜索，目前不支持全拼
	 */
	private void filterData(String filterStr) {
		String search = null;
		CharacterParser characterParser = CharacterParser.getInstance();
		// 新建一个SortModel类型的List
		List<CallInfo> filterDateList = new ArrayList<CallInfo>();

		// 判断EditText中是否为空
		if (TextUtils.isEmpty(filterStr)) {
			filterDateList = callList;
		} else {
			filterDateList.clear();
			for (CallInfo callInfo : callList) {
				search = callInfo.getName() + callInfo.getPhone();

				if (search != null) {
					// 返回字符中indexof（string）中字串string在父串中首次出现的位置，从0开始！没有返回-1；方便判断和截取字符串！

					// if(name.indexOf(filterStr.toString()) != -1 ||
					// characterParser.getSelling(name).startsWith(filterStr.toString())){
					if (search.indexOf(filterStr.toString()) != -1
							|| characterParser.getSelling(search).startsWith(
									filterStr.toString())) {
						// filterDateList中的数据改变

						filterDateList.add(callInfo);
					}
				}
			}
		}

		callListAdapter.updateListView(filterDateList);
	}
	

}
	
