package holder;

import android.widget.ImageView;
import android.widget.TextView;

public class SmsHolder {
	TextView sms_date;
	TextView sms_body;
	ImageView sms_status;
	
	public TextView getSms_date() {
		return sms_date;
	}
	public void setSms_date(TextView sms_date) {
		this.sms_date = sms_date;
	}
	public TextView getSms_body() {
		return sms_body;
	}
	public void setSms_body(TextView sms_body) {
		this.sms_body = sms_body;
	}
	public ImageView getSms_status() {
		return sms_status;
	}
	public void setSms_status(ImageView sms_status) {
		this.sms_status = sms_status;
	}
}
