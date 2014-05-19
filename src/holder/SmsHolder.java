package holder;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SmsHolder {
	LinearLayout sms_title;
	LinearLayout sms_line;
	TextView sms_date;
	TextView sms_body;
	ImageView sms_status;
	

	public LinearLayout getSms_title() {
		return sms_title;
	}
	public void setSms_title(LinearLayout sms_title) {
		this.sms_title = sms_title;
	}
	public LinearLayout getSms_line() {
		return sms_line;
	}
	public void setSms_line(LinearLayout sms_line) {
		this.sms_line = sms_line;
	}
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
