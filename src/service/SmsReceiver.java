package service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class SmsReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		// 表示对方成功收到短信
		boolean isMass = intent.getBooleanExtra("isMass", false);
		if(!isMass){
		Toast.makeText(context, "对方接收成功", Toast.LENGTH_LONG).show();
		}
	}
};
