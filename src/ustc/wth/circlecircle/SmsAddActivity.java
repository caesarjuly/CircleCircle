package ustc.wth.circlecircle;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import entity.ContactInfo;
import entity.ConversationInfo;
import entity.SmsInfo;
import service.ContactService;
import service.SmsService;
import ustc.wth.circlecircle.ConversationActivity.mReceiver;
import ustc.wth.circlecircle.ConversationActivity.mSender;
import utils.ChipsMultiAutoCompleteTextview;
import utils.Threads;
import utils.Uris;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

public class SmsAddActivity extends Activity{

	ChipsMultiAutoCompleteTextview mu;
	private ContactService ct;
	private List<ContactInfo> contact_infos;
	private HashMap<String, Integer> nameToId;
	private ContactService contact;
	private EditText sms_input;
	private Button sms_send;
	private SmsService sms;
	private BroadcastReceiver mSender;
	static int distinction = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_sms);
		sms_input = (EditText) findViewById(R.id.sms_input);
		sms_send = (Button) findViewById(R.id.sms_send);
		
		ct = new ContactService(this);
		nameToId = new HashMap<String, Integer>();
		sms = new SmsService(this);
		contact = new ContactService(this);
		contact_infos = contact.getContactInfoWithoutGroup();
		mu = (ChipsMultiAutoCompleteTextview) findViewById(R.id.multiAutoCompleteTextView1);

		String[] item = new String[contact_infos.size()]; 
		for(int i=0; i<contact_infos.size(); i++){
			item[i] = contact_infos.get(i).getName();
			nameToId.put(item[i], contact_infos.get(i).getId());
		}
		

		mu.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, item));
		mu.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
		
		sms_input.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				/* ++ 文本每次改变就会跑这个方法 ++ */
				sms_send.setBackgroundResource(R.drawable.ic_send_disable);
				if (s.length() > 0) {
					sms_send.setBackgroundResource(R.drawable.ic_send);
				}
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}
		});
		sms_send.setOnTouchListener(new Button.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (v.getId() == R.id.sms_send && sms_input.length() > 0) {
					if (event.getAction() == MotionEvent.ACTION_UP) {
						sms_send.setBackgroundResource(R.drawable.ic_send);
					}
					if (event.getAction() == MotionEvent.ACTION_DOWN) {
						sms_send.setBackgroundResource(R.drawable.ic_send_press);
					}
				}
				return false;
			}

		});
		sms_send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				boolean isMass = true;
				String ci = mu.getText().toString();
				if(ci.charAt(ci.length()-1) == ' ' && ci.charAt(ci.length()-2) == ','){
					ci = ci.substring(0, ci.length()-2);
				}
				String[] cis = ci.split(", ");
				String[] phones = new String[cis.length];
				ContactInfo[] ciArray = new ContactInfo[cis.length];
				for(int i=0; i< cis.length; i++){
					if(nameToId.containsKey(cis[i])){
						ciArray[i] = ct.getContactById(nameToId.get(cis[i]), cis[i]);
						phones[i] = ciArray[i].getPhone();	
					}else{
						phones[i] = cis[i];
						ciArray[i] = new ContactInfo();
						ciArray[i].setName(phones[i]);
						ciArray[i].setPhone(phones[i]);
					}
				}
				Set<String> phoneSet = new HashSet<String>(Arrays.asList(phones));
				long convId = Threads.getOrCreateThreadId(getApplicationContext(), phoneSet);
					
					if(cis.length == 1){
						isMass = false;
					}
					

					Bundle b = new Bundle();
					// 生成一个Intent对象
					Intent intent = new Intent();
					if(!isMass){
						intent.putExtra("contactName", cis[0]);
						intent.putExtra("phone", phones[0]);
						b.putParcelable("photo", ct.getConvPhotoByPhone(phones[0]));
					}else{
						b.putParcelableArray("contactInfos", ciArray);
					}
					
					intent.putExtra("id", convId);
					intent.putExtra("isMass", isMass);
					intent.putExtra("name", ci);
					intent.putExtras(b);
					intent.putExtra("content", sms_input.getText().toString());
					intent.putExtra("isTask", true);
					intent.setClass(getApplicationContext(), ConversationActivity.class);
					// 通过Intent对象启动另外一个Activity
					startActivity(intent);
					sms_input.clearFocus();
					sms_input.setText("");
				}
	});
	

}
}
