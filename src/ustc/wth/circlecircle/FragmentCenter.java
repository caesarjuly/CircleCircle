package ustc.wth.circlecircle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;










import service.ContactService;
import entity.ContactInfo;
import entity.GroupInfo;
import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

public class FragmentCenter extends Fragment{
	
	private LayoutInflater layoutinflater;
	private View myview;
	private Context context;
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
	
	
