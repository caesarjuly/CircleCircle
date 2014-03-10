package ustc.wth.circlecircle;

import java.util.ArrayList;









import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class Add_contact extends Activity {
	private ImageButton imgbutton;
	private EditText edittext_name,edittext_phone,edittext_address,edittext_email;
	private Button button;
	String TAG="abc";
	private TextView textview1,textview2,textview3;
	private TextView textview_phone,textview_emial,textview_address;
	
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_contact);
        
        //获取edittext
        edittext_name=(EditText)findViewById(R.id.edit_name);
        edittext_phone=(EditText)findViewById(R.id.edit_num);
        edittext_email=(EditText)findViewById(R.id.edit_email);
        edittext_address=(EditText)findViewById(R.id.edit_address);
        
        //初始化界面内容
        textview1=(TextView)findViewById(R.id.textview_view1);
        textview1.setText("电话");
        textview_phone=(TextView)findViewById(R.id.textview_phone);
        textview_phone.setText("手机");
        textview2=(TextView)findViewById(R.id.textview_view2);
        textview2.setText("email");
        textview_address=(TextView)findViewById(R.id.textview_email);
        textview_address.setText("邮箱");
        textview3=(TextView)findViewById(R.id.textview_view3);
        textview3.setText("地址");
        textview_address=(TextView)findViewById(R.id.textview_address);
        textview_address.setText("住宅地址");
        button=(Button)findViewById(R.id.addcon_button);
        button.setText("完成");
        
        //添加自定义 
        MyButtonClickListener clickListener = new MyButtonClickListener();   
        
        button.setOnClickListener(clickListener); 
        
        Log.i(TAG,"123");
        //final String string = edittext_name.getHint().toString();
        edittext_name.setOnFocusChangeListener(new OnFocusChangeListener(){
            public void onFocusChange(View arg0, boolean hasFocus) {
                if(hasFocus){
                	Log.i(TAG,"456");
                	edittext_name.setHint(null);
                }else{
                	//edittext_name.setHint(string);
                }
            }
            
        });
        


    }
	
	  class MyButtonClickListener implements OnClickListener   
	    {  
	        public void onClick(View v)   
	        {  
	        	insert();
	        	//addContact();  	
	        	//如果添加成功，显示“添加成功”
	        	Toast.makeText(Add_contact.this,
	    				"添加成功 ", Toast.LENGTH_SHORT)
	    				.show();
	        }  
	    }  
	  
	  protected void createContactEntry() {  
		     
		    ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();  
	
		    ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)  
		            .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)  
		            .withValue(ContactsContract.Data.MIMETYPE,  
		                    ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)  
		            .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, "小明")  
		            .build());  
		    ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)  
		            .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)  
		            .withValue(ContactsContract.Data.MIMETYPE,  
		                    ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)  
		            .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, "10086")  
		            .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, "1")  
		            .build());  
		    ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)  
		            .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)  
		            .withValue(ContactsContract.Data.MIMETYPE,  
		                    ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)  
		            .withValue(ContactsContract.CommonDataKinds.Email.DATA, "google@sina.com")  
		            .withValue(ContactsContract.CommonDataKinds.Email.TYPE, "1")  
		            .build());  
		  
		    try {  
		        getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);  
		    } catch (Exception e) {  
		        Log.e(TAG, "Exceptoin encoutered while inserting contact: " + e);  
		    }  
		}  
	  
	  public void insert()   
	    {   
		  
			String name="";
			String phone="";
			String email="";
			String address="";
			name=edittext_name.getText().toString();
			phone=edittext_phone.getText().toString();
			email=edittext_email.getText().toString();
			address=edittext_address.getText().toString();
		  
	    //首先插入空值，再得到rawContactsId ，用于下面插值   
	    ContentValues values = new ContentValues ();   
	    //insert a null value  
	    Uri rawContactUri = getContentResolver().insert(RawContacts.CONTENT_URI,values);   
	    long rawContactsId = ContentUris.parseId(rawContactUri);   
	  
	    //往刚才的空记录中插入姓名   
	    values.clear();   
	    //A reference to the _ID that this data belongs to  
	    values.put(StructuredName.RAW_CONTACT_ID,rawContactsId);   
	    //"CONTENT_ITEM_TYPE" MIME type used when storing this in data table  
	    values.put(Data.MIMETYPE,StructuredName.CONTENT_ITEM_TYPE);   
	    //The name that should be used to display the contact.  
	    values.put(StructuredName.DISPLAY_NAME,name);   
	    //insert the real values  
	    getContentResolver().insert(Data.CONTENT_URI,values);   
	    //插入电话   
	    values.clear();   
	    values.put(Phone.RAW_CONTACT_ID,rawContactsId);   
	    //String "Data.MIMETYPE":The MIME type of the item represented by this row  
	    //String "CONTENT_ITEM_TYPE": MIME type used when storing this in data table.  
	    values.put(Data.MIMETYPE,Phone.CONTENT_ITEM_TYPE);   
	    values.put(Phone.NUMBER,phone);   
	    getContentResolver().insert(Data.CONTENT_URI,values);   
	    }  
	  
	  

	
//	private void addContact(){
//		
//		Context context=this.getApplicationContext();
//		String name="";
//		String phone="";
//		String email="";
//		String address="";
//		name=edittext_name.getText().toString();
//		phone=edittext_phone.getText().toString();
//		email=edittext_email.getText().toString();
//		address=edittext_address.getText().toString();
//		
//		ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
//
//		//在名片表插入一个新名片
//		ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
//		.withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null).withValue(ContactsContract.RawContacts._ID, 0).withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null).withValue(
//		ContactsContract.RawContacts.AGGREGATION_MODE,ContactsContract.RawContacts.AGGREGATION_MODE_DISABLED).build());
//		
//		Log.i(TAG,"id号"+ContactsContract.RawContacts._ID.toString());
//		// add name
//		//添加一条新名字记录；对应RAW_CONTACT_ID为0的名片
//		if (!name.equals("")) {
//		ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
//		.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0).withValue(
//		ContactsContract.Data.MIMETYPE,ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE).withValue(
//		ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,name).build());
//		}
//
//		//添加昵称
////		ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
////		.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0).withValue(
////		ContactsContract.Data.MIMETYPE,ContactsContract.CommonDataKinds.Nickname.CONTENT_ITEM_TYPE).withValue(ContactsContract.CommonDataKinds.Nickname.NAME,"Anson昵称").build());
//
//		// add company
////		if (!organisation.equals("")) {
////		ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI).withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0).withValue(ContactsContract.Data.MIMETYPE,
////		ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE).withValue(
////		ContactsContract.CommonDataKinds.Organization.COMPANY,organisation).withValue(
////		ContactsContract.CommonDataKinds.Organization.TYPE,ContactsContract.CommonDataKinds.Organization.TYPE_WORK).build());
////		}
//
//		// add phone
//		if (!phone.equals("")) {
//		ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
//		.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
//		.withValue(ContactsContract.Data.MIMETYPE,
//		ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE).withValue(ContactsContract.CommonDataKinds.Phone.NUMBER,phone).withValue(ContactsContract.CommonDataKinds.Phone.TYPE,1).build());
//		}
//
//		// add Fax
////		if (!fax.equals("")) {
////		ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI).withValueBackReference(
////		ContactsContract.Data.RAW_CONTACT_ID, 0).withValue(
////		ContactsContract.Data.MIMETYPE,
////		ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE).withValue(
////		ContactsContract.CommonDataKinds.Phone.NUMBER,fax)
////		.withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
////		ContactsContract.CommonDataKinds.Phone.TYPE_FAX_WORK).build());
////		}
//
//
//
//		// add email
//		if (!email.equals("")) {
//		ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
//		.withValueBackReference(
//		ContactsContract.Data.RAW_CONTACT_ID, 0).withValue(
//		ContactsContract.Data.MIMETYPE,ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE).withValue(ContactsContract.CommonDataKinds.Email.DATA,email).withValue(ContactsContract.CommonDataKinds.Email.TYPE,1).build());
//		}
//
//		// add address
//		if (!address.equals("")) {
//		ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI).withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0).withValue(
//		ContactsContract.Data.MIMETYPE,ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE).withValue(
//		ContactsContract.CommonDataKinds.StructuredPostal.STREET,address)
//		.withValue(ContactsContract.CommonDataKinds.StructuredPostal.TYPE,
//		ContactsContract.CommonDataKinds.StructuredPostal.TYPE_WORK).build());
//		}
//
//		// add website
////		if (!website.equals("")) {
////		ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
////		.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
////		.withValue(ContactsContract.Data.MIMETYPE,
////		ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE).withValue(
////		ContactsContract.CommonDataKinds.Website.URL,website)
////		.withValue(
////		ContactsContract.CommonDataKinds.Website.TYPE,
////		ContactsContract.CommonDataKinds.Website.TYPE_WORK).build());
////		}
//
//		// add IM
////		String qq1="452824089";
////		ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI).withValueBackReference(
////		ContactsContract.Data.RAW_CONTACT_ID, 0).withValue(
////		ContactsContract.Data.MIMETYPE,
////		ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE).withValue(
////		ContactsContract.CommonDataKinds.Im.DATA1,qq1)
////		.withValue(
////		ContactsContract.CommonDataKinds.Im.PROTOCOL,
////		ContactsContract.CommonDataKinds.Im.PROTOCOL_QQ).build());
//
//		// add logo image
//		// Bitmap bm = logo;
//		// if (bm != null) {
//		// ByteArrayOutputStream baos = new ByteArrayOutputStream();
//		// bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
//		// byte[] photo = baos.toByteArray();
//		// if (photo != null) {
//		//
//		// ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
//		// .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
//		// .withValue(ContactsContract.Data.MIMETYPE,
//		// ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE)
//		// .withValue(ContactsContract.CommonDataKinds.Photo.PHOTO, photo)
//		// .build());
//		// }
//		// }
//
//		try {
//		context.getContentResolver().applyBatch(
//		ContactsContract.AUTHORITY, ops);
//		} catch (Exception e){
//		}
//
//		}

	


}
