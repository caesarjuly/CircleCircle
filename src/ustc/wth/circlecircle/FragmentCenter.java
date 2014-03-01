package ustc.wth.circlecircle;

import service.ContactService;
import service.SmsService;
import ustc.wth.circlecircle.FragmentMessage.SmsListAdapter;
import global.Uris;
import imagebutton.Imagetext_button;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;

public class FragmentCenter extends Fragment{
	
	private Imagetext_button bt1;
	private LayoutInflater layoutinflater;
	private View myview;
	private Context context;


 	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {	

 		return inflater.inflate(R.layout.fragment_center, null);		
 	}	
  }
	
	
