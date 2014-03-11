package ustc.wth.circlecircle;

import java.util.List;
import service.SmsService;
import android.support.v4.app.ListFragment;
import android.net.Uri;
import android.os.Bundle;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import entity.ConversationInfo;
import adapter.*;

public class FragmentMessage extends ListFragment implements
		OnItemLongClickListener {
	private List<ConversationInfo> conversations;
	private SmsService sms;
	private PopupWindow pw;
	private SmsListAdapter smsListAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_message, container, false);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sms = new SmsService(this.getActivity());
		conversations = sms.getSmsInfo();
		smsListAdapter = new SmsListAdapter(this.getActivity(), conversations);
		setListAdapter(smsListAdapter);
		String unReadNum = sms.getUnreadNum(); // 获取新信息数目
		Toast.makeText(getActivity(), "您有" + unReadNum + "条新消息！",
				Toast.LENGTH_LONG).show();
	}

	public void onListItemClick(ListView parent, View v, int position, long id) {
		// 生成一个Intent对象
		Intent intent = new Intent();
		// 在Intent对象当中添加一个键值对
		intent.putExtra("testIntent", "123");
		// 设置Intent对象要启动的Activity
		intent.setClass(getActivity(), ConversationActivity.class);
		// 通过Intent对象启动另外一个Activity
		startActivity(intent);
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		LinearLayout pv = (LinearLayout) LayoutInflater.from(
				getActivity()).inflate(R.layout.pop_up_menu, null);

		pw = new PopupWindow(getActivity());
		pw.setContentView(pv);
		Drawable dw = getResources().getDrawable(R.drawable.qzone_bg_copy);
		pw.setBackgroundDrawable(dw);

		TextView tvCall = (TextView) pv.findViewById(R.id.thread_call);
		TextView tvDel = (TextView) pv.findViewById(R.id.thread_delete);
		ImageView ivLine = (ImageView) pv.findViewById(R.id.line);
		
		ConversationInfo ci = conversations.get(arg2);
		if(ci.getIsMass() == 1){
			tvCall.setVisibility(View.GONE);
			ivLine.setVisibility(View.GONE);
		}

		// popwindow的长和宽的，必须要设置的，不然无法显示的
		pw.setWidth(400);
		pw.setHeight(150);

		pw.setOutsideTouchable(true);
		pw.setFocusable(true);

		View line = arg1;
        pw.showAsDropDown(line, line.getWidth()/2-200, -line.getHeight()-75);
         
        tvCall.setOnClickListener(new TelCallListener(ci.getCti().getPhone()));
        tvDel.setOnClickListener(new ConDelListener(ci.getId(), ci));
		return false;
	}

	class TelCallListener implements OnClickListener{
		
		private String phone;
		
		TelCallListener(String phone){
			this.phone = phone;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			pw.dismiss();
			Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+phone));  
            startActivity(intent); 
            pw= null;
		}
		
	}
	
	class ConDelListener implements OnClickListener{
		
		private int id;
		private ConversationInfo ci;
		ConDelListener(int id, ConversationInfo ci){
			this.ci = ci;
			this.id = id;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			pw.dismiss();
			sms.deleteConversation(id);
			smsListAdapter.removeConversation(ci);
			smsListAdapter.notifyDataSetChanged();
            pw= null;
		}
		
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		getListView().setOnItemLongClickListener(this);
	}

}