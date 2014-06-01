package service;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class SmsSender extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// 判断短信是否发送成功
		boolean isMass = intent.getBooleanExtra("isMass", false);
		switch (getResultCode()) {
		case Activity.RESULT_OK:
			if(!isMass){
				Toast.makeText(context, "短信发送成功", Toast.LENGTH_SHORT).show();
			}
			String uri = intent.getStringExtra("result");
			SmsService smsService = new SmsService();
			smsService.update(uri);
			break;
		default:
			Toast.makeText(context, "发送失败", Toast.LENGTH_LONG).show();
			break;
		}
	}
};
