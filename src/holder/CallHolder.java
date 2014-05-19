package holder;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class CallHolder {
	TextView body;
	TextView name;
	ImageView in_out;
	ImageView call_image;
	TextView date;
	TextView img;
	int position;
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	public TextView getBody() {
		return body;
	}
	public void setBody(TextView body) {
		this.body = body;
	}
	public TextView getName() {
		return name;
	}
	public void setName(TextView name) {
		this.name = name;
	}
	public ImageView getIn_out() {
		return in_out;
	}
	public void setIn_out(ImageView in_out) {
		this.in_out = in_out;
	}
	public ImageView getCall_image() {
		return call_image;
	}
	public void setCall_image(ImageView call_image) {
		this.call_image = call_image;
	}
	public TextView getDate() {
		return date;
	}
	public void setDate(TextView date) {
		this.date = date;
	}
	public TextView getImg() {
		return img;
	}
	public void setImg(TextView img) {
		this.img = img;
	}

	
}
