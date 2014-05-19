package ustc.wth.circlecircle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import service.SmsService;
import ustc.wth.circlecircle.FragmentConversation.ConDelListener;
import ustc.wth.circlecircle.FragmentConversation.TelCallListener;
import utils.Uris;

import entity.ContactInfo;
import entity.ConversationInfo;
import entity.SmsInfo;
import adapter.SmsListAdapter;
import android.app.Activity;
import android.app.ListActivity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class ConversationActivity extends ListActivity {
	private long convId;
	private boolean isMass;
	private String name;
	private SmsListAdapter smsListAdapter;
	private SmsService sms;
	private TextView sms_title;
	private TextView sms_photo;
	private TextView sms_phone;
	private EditText sms_input;
	private Button sms_send;
	private String phone;
	private ContactInfo[] cis;
	private List<SmsInfo> smsList;
	private SmsInfo smsInfo;
	private PopupWindow pw;
	private String copy;
	private BroadcastReceiver mSender;
	private BroadcastReceiver mReceiver;
	private HashMap<String, String> phoneToName = new HashMap<String, String>();
	static int distinction = 0;

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.sms);
		sms_title = (TextView) findViewById(R.id.sms_title);
		sms_photo = (TextView) findViewById(R.id.sms_photo);
		sms_phone = (TextView) findViewById(R.id.sms_phone);
		sms_input = (EditText) findViewById(R.id.sms_input);
		sms_send = (Button) findViewById(R.id.sms_send);
		
		sms = new SmsService(this);
		

		Intent intent = this.getIntent();
		convId = intent.getExtras().getLong("id");
		isMass = intent.getBooleanExtra("isMass", false);
		name = (String) intent.getCharSequenceExtra("name");
		Bitmap photo = (Bitmap) intent.getExtras().get("photo");
		if(intent.getExtras().containsKey("contactInfos"))
		{
			 Parcelable[] parcels = intent.getExtras().getParcelableArray("contactInfos");
			 cis = new ContactInfo[parcels.length];
			 for (int i = 0; i < parcels.length; i++) {
				 cis[i] = (ContactInfo) parcels[i];
				 phoneToName.put(cis[i].getPhone(), cis[i].getName());
			 } 
		}
		String contactName = (String) intent
				.getCharSequenceExtra("contactName");
		phone = (String) intent.getCharSequenceExtra("phone");

		sms_title.setText(name);
		sms_phone.setText(phone);
		if (isMass) {
			sms_phone.setVisibility(View.GONE);
			sms_photo.setBackgroundResource(R.drawable.ic_contacts_picture);
		} else {
			if (photo == null) {
				if (contactName == null) {
					sms_phone.setVisibility(View.GONE);
					sms_photo
							.setBackgroundResource(R.drawable.ic_contact_picture);
				} else {
					String txt = name.substring(0, 1);
					sms_photo.setText(txt);
					sms_photo.setBackgroundColor(getResources().getColor(
							R.color.lightgray));
				}

			} else {
				Drawable bd = new BitmapDrawable(getResources(), photo);
				sms_photo.setBackgroundDrawable(bd);
			}
		}
		
		smsList = sms.getSmsByConvId(convId);
		smsListAdapter = new SmsListAdapter(this.getApplicationContext(),
				smsList, isMass, phoneToName);
		setListAdapter(smsListAdapter); 
		getContentResolver().registerContentObserver(Uri.parse(Uris.SMS_URI_ALL), true, new SmsObserver(new Handler()));
		
		if(intent.getExtras().containsKey("isTask")){
			String content = intent.getExtras().getString("content");
			List<String> phones = new ArrayList<String>();
			if(!isMass){
					SmsInfo newSms = new SmsInfo();
					newSms.setDate(System.currentTimeMillis());
					newSms.setBody(content);
					newSms.setRead(0);
					newSms.setType(2);
					newSms.setStatus(64);
					newSms.setAddress(phone);
					newSms.setThread_id(convId);
					Uri result = sms.addSms(newSms);
					phones.add(phone);
					sendSMS(phone, content, result);
				}else{
					for(int i=0; i<cis.length;i++){
						SmsInfo newSms = new SmsInfo();
						newSms.setDate(System.currentTimeMillis());
						newSms.setBody(content);
						newSms.setRead(0);
						newSms.setType(2);
						newSms.setStatus(64);
						newSms.setAddress(cis[i].getPhone());
						newSms.setThread_id(convId);
						Uri result = sms.addSms(newSms);
						sendSMS(cis[i].getPhone(), content, result);
					}
				}
		}

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
		sms_send.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				List<String> phones = new ArrayList<String>();
				if (sms_input.length() > 0) {
					if(!isMass){
						SmsInfo newSms = new SmsInfo();
						newSms.setDate(System.currentTimeMillis());
						newSms.setBody(sms_input.getText().toString());
						newSms.setRead(0);
						newSms.setType(2);
						newSms.setStatus(64);
						newSms.setAddress(phone);
						newSms.setThread_id(convId);
						Uri result = sms.addSms(newSms);
						phones.add(phone);
						sendSMS(phone, sms_input.getText().toString(), result);
					}else{
						for(int i=0; i<cis.length;i++){
							SmsInfo newSms = new SmsInfo();
							newSms.setDate(System.currentTimeMillis());
							newSms.setBody(sms_input.getText().toString());
							newSms.setRead(0);
							newSms.setType(2);
							newSms.setStatus(64);
							newSms.setAddress(cis[i].getPhone());
							newSms.setThread_id(convId);
							Uri result = sms.addSms(newSms);
							sendSMS(cis[i].getPhone(), sms_input.getText().toString(), result);
						}
					}
					sms_input.clearFocus();
					sms_input.setText("");
				}
			}

		});


		ListView lv = getListView(); 
		lv.setOnItemLongClickListener(new OnItemLongClickListener(){
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				LinearLayout pv = (LinearLayout) LayoutInflater.from(arg1.getContext())
						.inflate(R.layout.pop_up_conversation, null);

				pw = new PopupWindow(arg1.getContext());
				pw.setContentView(pv);
				Drawable dw = getResources().getDrawable(R.drawable.qzone_bg_copy);
				pw.setBackgroundDrawable(dw);

				TextView smsCopy = (TextView) pv.findViewById(R.id.conversation_call);
				TextView smsDel = (TextView) pv.findViewById(R.id.conversation_delete);
				ImageView ivLine = (ImageView) pv.findViewById(R.id.line);
				smsCopy.setText("复制");

				smsInfo = smsList.get(arg2);
				copy = smsInfo.getBody();
				
				smsCopy.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						pw.dismiss();
						ClipboardManager clip = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
						clip.setText(copy); 
						pw = null;
					}});

				// popwindow的长和宽的，必须要设置的，不然无法显示的
				pw.setWidth(400);
				pw.setHeight(150);

				pw.setOutsideTouchable(true);
				pw.setFocusable(true);

				View line = arg1;
				pw.showAsDropDown(line, line.getWidth() / 2 - 200,
						-line.getHeight() - 75);

				smsDel.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						pw.dismiss();
						sms.deleteSms(smsInfo.getId());
						smsListAdapter.removeSms(smsInfo);
						smsListAdapter.notifyDataSetChanged();
						pw = null;
					}});
				return false;
			}
		});
		
		lv.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				sms_title.setFocusable(true);
				  sms_title.setFocusableInTouchMode(true);
				  sms_title.requestFocus();  // 初始不让EditText得焦点
				  sms_title.requestFocusFromTouch();
			}
			
		});
		}
	
	private final class SmsObserver extends ContentObserver{  
        public SmsObserver(Handler handler) {  
            super(handler);  
        }  
        @Override  
        public void onChange(boolean selfChange) {  
            smsList = sms.getSmsByConvId(convId);
            smsListAdapter.updateListView(smsList);
            }  
        } 

	class mSender extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// 判断短信是否发送成功
			switch (getResultCode()) {
			case Activity.RESULT_OK:
				if(!isMass){
					Toast.makeText(context, "短信发送成功", Toast.LENGTH_SHORT).show();
				}
				String uri = intent.getStringExtra("result");
				SmsService smsService = new SmsService((Activity) context);
				smsService.update(uri);
				break;
			default:
				Toast.makeText(context, "发送失败", Toast.LENGTH_LONG).show();
				break;
			}
		}
	};
	class mReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			// 表示对方成功收到短信
			if(!isMass){
				Toast.makeText(context, "对方接收成功", Toast.LENGTH_LONG).show();
			}
			}
	};

	private void sendSMS(String phoneNumber, String message, Uri result) {
		// ---sends an SMS message to another device---
		SmsManager smsMgr = SmsManager.getDefault();
		
		// create the sentIntent parameter
		Intent sentIntent = new Intent(Uris.SENT_SMS_ACTION);
		sentIntent.putExtra("result", result.toString());
		PendingIntent sentPI = PendingIntent.getBroadcast(this, distinction++, sentIntent,
				0);

		// create the deilverIntent parameter
		Intent deliverIntent = new Intent(Uris.DELIVERED_SMS_ACTION);
		PendingIntent deliverPI = PendingIntent.getBroadcast(this, distinction,
				deliverIntent, 0);

		// 如果短信内容超过70个字符 将这条短信拆成多条短信发送出去
		if (message.length() > 70) {
			ArrayList<String> msgs = smsMgr.divideMessage(message);
			for (String msg : msgs) {
				smsMgr.sendTextMessage(phoneNumber, null, msg, sentPI, deliverPI);
			}
		} else {
			smsMgr.sendTextMessage(phoneNumber, null, message, sentPI, deliverPI);
		}
	}
	
	@Override
    public boolean onTouchEvent(MotionEvent event) {
		sms_title.setFocusable(true);
		  sms_title.setFocusableInTouchMode(true);
		  sms_title.requestFocus();  // 初始不让EditText得焦点
		  sms_title.requestFocusFromTouch();
            return super.onTouchEvent(event);
    }
	
	

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		mSender = new mSender();
		mReceiver = new mReceiver();
		registerReceiver(mSender, new IntentFilter(Uris.SENT_SMS_ACTION));
		registerReceiver(mReceiver, new IntentFilter(Uris.DELIVERED_SMS_ACTION));
		super.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		unregisterReceiver(mSender);  
        unregisterReceiver(mReceiver); 
		super.onPause();
	}
}
