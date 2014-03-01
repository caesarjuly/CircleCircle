package ustc.wth.circlecircle;

import global.Uris;
import entity.SmsInfo;

import java.util.List;
import service.SmsService;
import android.support.v4.app.ListFragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;
import entity.ThreadInfo;
import adapter.*;

public class FragmentMessage extends ListFragment {
	private List<ThreadInfo> threads;
	private SmsService sms;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_message, container, false);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sms = new SmsService(this.getActivity());
		threads = sms.getSmsInfo();
		setListAdapter(new SmsListAdapter(this.getActivity(), threads));
		String unReadNum = sms.getUnreadNum();
		Toast.makeText(getActivity(), "您有" + unReadNum + "条新消息！",
				Toast.LENGTH_LONG).show();
	}

	public void onListItemClick(ListView parent, View v, int position, long id) {
		// 生成一个Intent对象
		Intent intent = new Intent();
		// 在Intent对象当中添加一个键值对
		intent.putExtra("testIntent", "123");
		// 设置Intent对象要启动的Activity
		intent.setClass(getActivity(), MessageThreadActivity.class);
		// 通过Intent对象启动另外一个Activity
		startActivity(intent);
	}

}