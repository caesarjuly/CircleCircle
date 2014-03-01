package ustc.wth.circlecircle;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class Add_contact extends Activity {
	private ImageButton imgbutton;
	private EditText edittext_name;
	private Button button;
	String TAG="abc";
	private TextView textview1;
	private TextView textview_phone;
	
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_contact);
        edittext_name=(EditText)findViewById(R.id.edit_name);
        //textview1=(TextView)findViewById(R.id.add_num);
        //textview1.setText("123");
        textview1=(TextView)findViewById(R.id.textview_view1);
        textview1.setText("电话");
        textview_phone=(TextView)findViewById(R.id.textview_phone);
        textview_phone.setText("手机");
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
        
        
//        imgbutton= (ImageButton) findViewById(R.id.contact_img);
//        
//        imgbutton.setBackgroundResource(R.drawable.ic_contact_picture);
        
        
        

    }
	
	
	public void InsertBatch() throws Exception { 
		
		    ContentResolver resolver = this.getContentResolver(); 
			//创建一个ArrayList保存联系人数据
		    ArrayList<ContentProviderOperation> operations = new ArrayList<ContentProviderOperation>(); 
		
		    ContentProviderOperation operation1 = ContentProviderOperation // 
		
		            .newInsert(Uri.parse("content://com.android.contacts/raw_contacts")) // 
		
		            .withValue("_id", null) // 
		
		            .build(); 
		
		    operations.add(operation1); 
		
		    ContentProviderOperation operation2 = ContentProviderOperation // 
		
		            .newInsert(Uri.parse("content://com.android.contacts/data")) // 
		
		            .withValueBackReference("raw_contact_id", 0) // 
		            	
		            .withValue("data2", "ZZH")  //联系人姓名	
		
		            .withValue("mimetype", "vnd.android.cursor.item/name") // 
		
		            .build(); 
		
		    operations.add(operation2); 
	
		
		    ContentProviderOperation operation3 = ContentProviderOperation // 
		
		            .newInsert(Uri.parse("content://com.android.contacts/data")) // 
		
		            .withValueBackReference("raw_contact_id", 0) // 
		
		            .withValue("data1", "18612312312") // 
		
		            .withValue("data2", "2") // 
		
		            .withValue("mimetype", "vnd.android.cursor.item/phone_v2") // 
		
		            .build(); 
		
		    operations.add(operation3); 
		
		
		    ContentProviderOperation operation4 = ContentProviderOperation // 
		
		            .newInsert(Uri.parse("content://com.android.contacts/data")) // 
		
		            .withValueBackReference("raw_contact_id", 0) // 
		
		            .withValue("data1", "zq@itcast.cn") // 
		
		            .withValue("data2", "2") // 
		
		            .withValue("mimetype", "vnd.android.cursor.item/email_v2") // 
		
		            .build(); 
		
		    operations.add(operation4); 
	
	
		    resolver.applyBatch("com.android.contacts", operations); 

		}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//添加姓名、公司、手机号码、email、地址、个人网站
	private void addContact(Context context, String name,
			String organisation,String phone, String fax, String email, String address,String website){

			ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

			//在名片表插入一个新名片
			ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
			.withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null).withValue(ContactsContract.RawContacts._ID, 0).withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null).withValue(
			ContactsContract.RawContacts.AGGREGATION_MODE,ContactsContract.RawContacts.AGGREGATION_MODE_DISABLED).build());

			// add name
			//添加一条新名字记录；对应RAW_CONTACT_ID为0的名片
			if (!name.equals("")) {
			ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
			.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0).withValue(
			ContactsContract.Data.MIMETYPE,ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE).withValue(
			ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,name).build());
			}

			//添加昵称
			ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
			.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0).withValue(
			ContactsContract.Data.MIMETYPE,ContactsContract.CommonDataKinds.Nickname.CONTENT_ITEM_TYPE).withValue(ContactsContract.CommonDataKinds.Nickname.NAME,"Anson昵称").build());

			// add company
			if (!organisation.equals("")) {
			ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI).withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0).withValue(ContactsContract.Data.MIMETYPE,
			ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE).withValue(
			ContactsContract.CommonDataKinds.Organization.COMPANY,organisation).withValue(
			ContactsContract.CommonDataKinds.Organization.TYPE,ContactsContract.CommonDataKinds.Organization.TYPE_WORK).build());
			}

			// add phone
			if (!phone.equals("")) {
			ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
			.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
			.withValue(ContactsContract.Data.MIMETYPE,
			ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE).withValue(ContactsContract.CommonDataKinds.Phone.NUMBER,phone).withValue(ContactsContract.CommonDataKinds.Phone.TYPE,1).build());
			}

			// add Fax
			if (!fax.equals("")) {
			ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI).withValueBackReference(
			ContactsContract.Data.RAW_CONTACT_ID, 0).withValue(
			ContactsContract.Data.MIMETYPE,
			ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE).withValue(
			ContactsContract.CommonDataKinds.Phone.NUMBER,fax)
			.withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
			ContactsContract.CommonDataKinds.Phone.TYPE_FAX_WORK).build());
			}



			// add email
			if (!email.equals("")) {
			ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
			.withValueBackReference(
			ContactsContract.Data.RAW_CONTACT_ID, 0).withValue(
			ContactsContract.Data.MIMETYPE,ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE).withValue(ContactsContract.CommonDataKinds.Email.DATA,email).withValue(ContactsContract.CommonDataKinds.Email.TYPE,1).build());
			}

			// add address
			if (!address.equals("")) {
			ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI).withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0).withValue(
			ContactsContract.Data.MIMETYPE,ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE).withValue(
			ContactsContract.CommonDataKinds.StructuredPostal.STREET,address)
			.withValue(ContactsContract.CommonDataKinds.StructuredPostal.TYPE,
			ContactsContract.CommonDataKinds.StructuredPostal.TYPE_WORK).build());
			}

			// add website
			if (!website.equals("")) {
			ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
			.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
			.withValue(ContactsContract.Data.MIMETYPE,
			ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE).withValue(
			ContactsContract.CommonDataKinds.Website.URL,website)
			.withValue(
			ContactsContract.CommonDataKinds.Website.TYPE,
			ContactsContract.CommonDataKinds.Website.TYPE_WORK).build());
			}

			// add IM
			String qq1="452824089";
			ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI).withValueBackReference(
			ContactsContract.Data.RAW_CONTACT_ID, 0).withValue(
			ContactsContract.Data.MIMETYPE,
			ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE).withValue(
			ContactsContract.CommonDataKinds.Im.DATA1,qq1)
			.withValue(
			ContactsContract.CommonDataKinds.Im.PROTOCOL,
			ContactsContract.CommonDataKinds.Im.PROTOCOL_QQ).build());

			// add logo image
			// Bitmap bm = logo;
			// if (bm != null) {
			// ByteArrayOutputStream baos = new ByteArrayOutputStream();
			// bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
			// byte[] photo = baos.toByteArray();
			// if (photo != null) {
			//
			// ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
			// .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
			// .withValue(ContactsContract.Data.MIMETYPE,
			// ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE)
			// .withValue(ContactsContract.CommonDataKinds.Photo.PHOTO, photo)
			// .build());
			// }
			// }

			try {
			context.getContentResolver().applyBatch(
			ContactsContract.AUTHORITY, ops);
			} catch (Exception e){
			}

			}

	
	
	
	
	
	
	
	
	
	
	


}
