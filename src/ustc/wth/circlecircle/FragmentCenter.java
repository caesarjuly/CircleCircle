package ustc.wth.circlecircle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import service.ContactService;
import entity.ContactInfo;
import entity.GroupInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

import entity.SmsInfo;
import service.SmsService;
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
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.telephony.SmsManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

public class FragmentCenter extends Fragment {

	private LayoutInflater layoutinflater;
	private View myview;
	private Context context;
	private List<ContactInfo> contact_infos;
	private ContactService contact;

	private EditText username;
	private EditText password;
	private TextView send_sms_web;
	private TextView textviewuser;
	private  ImageButton downbutton;
	private  ImageButton upbutton;
	private Button loginbutton;
	private Switch switchTest;
 	

	private  ImageButton imagebutton;

	private BroadcastReceiver mSender;
	private BroadcastReceiver mReceiver;
	ServerThread st;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		context = this.getActivity();
		
		return inflater.inflate(R.layout.fragment_center, null);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
	if (android.os.Build.VERSION.SDK_INT > 9) {
	    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	    StrictMode.setThreadPolicy(policy);
	}
		super.onCreate(savedInstanceState);
		
	}

	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		send_sms_web=(TextView)getActivity().findViewById(R.id.send_sms_web);
		username=(EditText)getActivity().findViewById(R.id.userlogin);
		password=(EditText)getActivity().findViewById(R.id.passlogin);
		upbutton=(ImageButton)getActivity().findViewById(R.id.imgbut1);
		downbutton=(ImageButton)getActivity().findViewById(R.id.imgbut2);
		loginbutton=(Button)getActivity().findViewById(R.id.loginbutton);
		loginbutton.setText("登录");
		MyButtonClickListener onclickListener = new MyButtonClickListener();  
		loginbutton.setOnClickListener(onclickListener);
		
		 switchTest = (Switch) getActivity().findViewById(R.id.switch_test);  
	        switchTest.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {  
	                    @Override  
	                    public void onCheckedChanged(CompoundButton buttonView,  
	                            boolean isChecked) {  
	                        Toast.makeText(context, "短信发送开启",  
	                                Toast.LENGTH_SHORT).show(); 
	                        if(isChecked){
	                        	st = new ServerThread();
	                        	st.start();
	                        }else{
	                        	st.stopThread();
	                        }
	                    }  
	                });  
	        
	        upbutton.setVisibility(View.GONE);
	        downbutton.setVisibility(View.GONE);
	        switchTest.setVisibility(View.GONE);
	        send_sms_web.setVisibility(View.GONE);
	}

	  class MyButtonClickListener implements OnClickListener   
	    {  
	        public void onClick(View v)   
	        {       	

	        	String name=username.getText().toString();
	        	String pass=password.getText().toString();
	        	String url = "android/android_login?username="+name+"&password="+pass;
	        	String res="";
	        	try{
	        		res=	HttpUtil.queryStringForPost(url);
		        		
	        	}
	        	catch(Exception e)
	        	{
	        		Toast.makeText(getActivity(), "用户名错误或者密码错误",
	        				Toast.LENGTH_LONG).show();
	        	}
	        	if(res.equals("\"OK\""))
	        	{
	        		Toast.makeText(getActivity(), "登录成功",
	        				Toast.LENGTH_LONG).show();
	        		upbutton.setVisibility(View.VISIBLE);
	    	        downbutton.setVisibility(View.VISIBLE);
	    	        switchTest.setVisibility(View.VISIBLE);
	    	        send_sms_web.setVisibility(View.VISIBLE);
	    	        username.setVisibility(View.GONE);
	    			password.setVisibility(View.GONE);
	    			upbutton.setVisibility(View.GONE);
	    			loginbutton.setVisibility(View.GONE);
	        		
	        	}
	        	else 
	        	{
	        		Toast.makeText(getActivity(), "用户名错误或者密码错误",
	        				Toast.LENGTH_LONG).show();
	        	}
      	
	        }  
	    }  
	

	// 创建一个线程在后台监听
	class ServerThread extends Thread {
		private static final int Port = 10000;
		ServerSocket serversocket = null;
		
		private boolean  _run  = true;
	    public void stopThread() {
	         this._run = false;
	    }

		public void run() {

			try {
				// 创建一个serversocket对象，并让他在Port端口监听
				serversocket = new ServerSocket(Port);
				while (_run) {
					// 调用serversocket的accept()方法，接收客户端发送的请求
					Socket socket = serversocket.accept();
					BufferedReader buffer = new BufferedReader(
							new InputStreamReader(socket.getInputStream()));
					// 读取数据
					String msg = buffer.readLine();
					// Toast.makeText(getActivity(), "您有" + msg + "条新消息！",
					// Toast.LENGTH_LONG).show();
					System.out.println("msg:" + msg);
					String[] temp = msg.split(",");
					Set<String> phoneSet = new HashSet<String>();
					phoneSet.add(temp[1]);
					long convId = Threads
							.getOrCreateThreadId(context, phoneSet);
					SmsService sms = new SmsService((Activity) context);
					if (msg.length() > 0) {
						SmsInfo newSms = new SmsInfo();
						newSms.setDate(System.currentTimeMillis());
						newSms.setBody(temp[0]);
						newSms.setRead(0);
						newSms.setType(2);
						newSms.setStatus(64);
						newSms.setAddress(temp[1]);
						newSms.setThread_id(convId);
						Uri result = sms.addSms(newSms);
						sendSMS(temp[1], temp[0], result);
					}
				}

			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					serversocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}


	private void sendSMS(String phoneNumber, String message, Uri result) {
		// ---sends an SMS message to another device---
		SmsManager smsMgr = SmsManager.getDefault();

		// create the sentIntent parameter
		Intent sentIntent = new Intent(Uris.SENT_SMS_ACTION);
		sentIntent.putExtra("result", result.toString());
		PendingIntent sentPI = PendingIntent.getBroadcast(this.getActivity(),
				0, sentIntent, 0);

		// create the deilverIntent parameter
		Intent deliverIntent = new Intent(Uris.DELIVERED_SMS_ACTION);
		PendingIntent deliverPI = PendingIntent.getBroadcast(
				this.getActivity(), 0, deliverIntent, 0);

		// 如果短信内容超过70个字符 将这条短信拆成多条短信发送出去
		if (message.length() > 70) {
			ArrayList<String> msgs = smsMgr.divideMessage(message);
			for (String msg : msgs) {
				smsMgr.sendTextMessage(phoneNumber, null, msg, sentPI,
						deliverPI);
			}
		} else {
			smsMgr.sendTextMessage(phoneNumber, null, message, sentPI,
					deliverPI);
		}
	}

	class mSender extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// 判断短信是否发送成功
			switch (getResultCode()) {
			case Activity.RESULT_OK:
				Toast.makeText(context, "发送成功", Toast.LENGTH_LONG).show();
				String uri = intent.getStringExtra("result");
				SmsService smsService = new SmsService((Activity) context);
				smsService.update(uri);
				break;
			default:
				break;
			}
		}
	};

	class mReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// 表示对方成功收到短信
			Toast.makeText(context, "对方接收成功", Toast.LENGTH_LONG).show();
		}
	};

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		mSender = new mSender();
		mReceiver = new mReceiver();
		context.registerReceiver(mSender,
				new IntentFilter(Uris.SENT_SMS_ACTION));
		context.registerReceiver(mReceiver, new IntentFilter(
				Uris.DELIVERED_SMS_ACTION));
		super.onResume();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		context.unregisterReceiver(mSender);
		context.unregisterReceiver(mReceiver);
		super.onPause();
	}
}
