package ustc.wth.circlecircle;

import java.util.ArrayList;
import java.util.List;

import global.Uris;
import entity.Contactinfo;
import service.ContactService;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentContact extends ListFragment{
	//联系人信息
	private List<Contactinfo> contact_infos;
	private ContactService contact;
	private ImageButton imgbutton;
	private Button button;
	private String TAG="abc";


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {	
		              	
		return inflater.inflate(R.layout.fragment_contact, null);		
	}	
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		Uri uri = Uri.parse(Uris.Contacts_URI_ALL);

		contact = new ContactService(this.getActivity(), uri);

		contact_infos = contact.getContactInfo();

		setListAdapter(new ContactListAdapter(this.getActivity()));

	}
	
	
	 public void onResume()   
	    {  
	        super.onResume();  
	        System.out.println("LeftFragment--->onResume");  
	     
	        button = (Button) getActivity().findViewById(R.id.button32);  
	        
	        button.setText("123");

	        MyButtonClickListener clickListener = new MyButtonClickListener();   
	        
	        button.setOnClickListener(clickListener);  

	    }  
	 
	 
	  class MyButtonClickListener implements OnClickListener   
	    {  
	        public void onClick(View v)   
	        {  
        	
	        	Intent intent=new Intent();

				intent.setClass(getActivity(), Add_contact.class);

				startActivity(intent);

	        	
	        }  
	    }  


	class ContactListAdapter extends BaseAdapter {
		private LayoutInflater layoutinflater;
		private View myview;	

		public ContactListAdapter(Context c) {	
			layoutinflater = LayoutInflater.from(c);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return contact_infos.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView != null) {
				myview = convertView;
			}else{
				myview = layoutinflater.inflate(R.layout.contact_line, null);
			}
			
			
			

			TextView name = (TextView) myview
					.findViewById(R.id.contact_name);
			TextView number = (TextView) myview
					.findViewById(R.id.contact_num);


			name.setText(contact_infos.get(position).getName());
			number.setText(contact_infos.get(position).getPhoneNumber());

			return myview;
		}

	}
	
	
	public void onListItemClick(ListView parent, View v, int position, long id) {
		Toast.makeText(getActivity(),
				"You have selected ", Toast.LENGTH_SHORT)
				.show();
	}
}