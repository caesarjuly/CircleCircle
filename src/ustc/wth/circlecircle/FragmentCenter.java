package ustc.wth.circlecircle;

<<<<<<< HEAD
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;










import service.ContactService;
import entity.ContactInfo;
import entity.GroupInfo;
=======
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

import entity.SmsInfo;

import service.SmsService;
import ustc.wth.circlecircle.ConversationActivity.mReceiver;
import ustc.wth.circlecircle.ConversationActivity.mSender;
import utils.Threads;
import utils.Uris;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
>>>>>>> d84a381a12c0f7cae4154dd7867e0d10f760910d
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
<<<<<<< HEAD
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
=======
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

public class FragmentCenter extends Fragment {
>>>>>>> d84a381a12c0f7cae4154dd7867e0d10f760910d

	private LayoutInflater layoutinflater;
	private View myview;
	private Context context;
<<<<<<< HEAD
	private List<ContactInfo> contact_infos;
	private ContactService contact;

	private  ImageButton imagebutton;
 	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {	

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
 		return inflater.inflate(R.layout.fragment_center, null);		
 	}	
 	
 	
	 public void onResume()   
	    {  
	        super.onResume();  
 	
	       
	        
	        contact = new ContactService(this.getActivity());
	        contact_infos = contact.getContactInfo();              //调用contactservice初始化数据
	        imagebutton = (ImageButton) getActivity().findViewById(R.id.imgbut1); 
	        Log.d("sssb","ssssb");
	        imagebutton.setOnClickListener(new View.OnClickListener() {  
	        	  
	            @Override  
	            public void onClick(View v) {  
	            	Log.d("sssb","ssssb");
	            	 GroupInfo groupinfo=new GroupInfo();
	     	        groupinfo.setName("groupname");
	     	        List list=new ArrayList<GroupInfo>();
	     	        list.add(groupinfo);
	            	ResponseJson json=new ResponseJson();
	    	        json.setDataEx(list);
	    	        String par="responseJson.dataEx="+list;
	    	        HttpUtil.queryStringForPost("android/android_synchronousContactor?"+par); 
	            }  
	        });  

	        
	        
	    }
 	
 	
  }
	
=======
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
		super.onCreate(savedInstanceState);
	}

>>>>>>> d84a381a12c0f7cae4154dd7867e0d10f760910d
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		 Switch switchTest = (Switch) getActivity().findViewById(R.id.switch_test);  
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
