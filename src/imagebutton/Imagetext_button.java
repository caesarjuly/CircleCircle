package imagebutton;

import ustc.wth.circlecircle.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Imagetext_button extends RelativeLayout {
	private ImageView imgView;  
    private TextView  textView;

    public Imagetext_button(Context context) {
        super(context,null);
    }
    
    public Imagetext_button(Context context,AttributeSet attributeSet) {
        super(context, attributeSet);
        
        LayoutInflater.from(context).inflate(R.layout.imgtext_button, this,true);
        
        this.imgView = (ImageView)findViewById(R.id.imgview);
        this.textView = (TextView)findViewById(R.id.textview);
        
        this.setClickable(true);
        this.setFocusable(true);
    }
    
    public void setImgResource(int resourceID) {
        this.imgView.setImageResource(resourceID);
    }
    
    public void setText(String text) {
        this.textView.setText(text);
    }
    
    public void setTextColor(int color) {
        this.textView.setTextColor(color);
    }
    
    public void setTextSize(float size) {
        this.textView.setTextSize(size);
    }

}
