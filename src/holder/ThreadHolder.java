package holder;

import android.widget.ImageView;
import android.widget.TextView;

public class ThreadHolder {
	int position;
	TextView body;
	TextView name;
	TextView messageCount;
	TextView date;
	TextView img;
	TextView num;
	TextView catalog;
	ImageView notice;
	
	
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
	public void setNum(TextView num){
		this.num=num;
	}
	public TextView getNum(){
		return num;
	}
	public TextView getMessageCount() {
		return messageCount;
	}
	public void setMessageCount(TextView messageCount) {
		this.messageCount = messageCount;
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
	public TextView getCatalog() {
		return catalog;
	}
	public void setCatalog(TextView catalog) {
		this.catalog = catalog;
	}
	
	
	public ImageView getNotice() {
		return notice;
	}
	public void setNotice(ImageView notice) {
		this.notice = notice;
	}
}
