package ustc.wth.circlecircle;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ConversationActivity extends ListActivity {
	private int convId;
	private boolean isMass;
	private String name;
	private TextView sms_title;
	private TextView sms_photo;
	private TextView sms_phone;
	public void onCreate(Bundle savedInstanceState) {
		
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sms);
        sms_title = (TextView) findViewById(R.id.sms_title);
        sms_photo = (TextView) findViewById(R.id.sms_photo);
        sms_phone = (TextView) findViewById(R.id.sms_phone);
        Intent intent = this.getIntent();
        convId = intent.getIntExtra("id", 0);
        isMass = intent.getBooleanExtra("isMass", false);
        name = (String) intent.getCharSequenceExtra("name");
        Bitmap photo = (Bitmap) intent.getExtras().get("photo");
        String contactName = (String) intent.getCharSequenceExtra("contactName");
        String phone = (String) intent.getCharSequenceExtra("phone");
        
        sms_title.setText(name);
        sms_phone.setText(phone);
        if(isMass){
        	sms_phone.setVisibility(View.GONE);
        	sms_photo.setBackgroundResource(R.drawable.ic_contacts_picture);
        }else{
        	if(photo == null){
        		if(contactName == null){
        			sms_phone.setVisibility(View.GONE);
        			sms_photo.setBackgroundResource(R.drawable.ic_contact_picture);
        		}else{
        			String txt = name.substring(0, 1);
            		sms_photo.setText(txt);
            		sms_photo.setBackgroundColor(getResources().getColor(R.color.lightgray));
        		}
        		
        	}else{
        		Drawable bd = new BitmapDrawable(getResources(), photo);
        		sms_photo.setBackgroundDrawable(bd);
        	}
        }
    }
}
