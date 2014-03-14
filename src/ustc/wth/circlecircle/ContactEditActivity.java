package ustc.wth.circlecircle;

import java.util.ArrayList;

import entity.ContactInfo;
import service.ContactService;
import ustc.wth.circlecircle.ContactAddActivity.MyButtonClickListener;
import utils.Uris;
import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts;
import android.provider.Contacts.People;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Im;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.CommonDataKinds.StructuredPostal;
import android.provider.ContactsContract.Data;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class ContactEditActivity extends Activity{
	private EditText edittext_name,edittext_phone,edittext_email,edittext_address,edittext_im;
	private TextView textview_phone,textview_email,textview_im,textview_address;
	private ImageButton imagebutton;
	private ContactService cts;
	
	
	
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_edit);
        
        String companyemail="";
        String QQ="";
        String personstreet="";
        String name="";
        String phone="";
        
        Context ctx=this.getApplicationContext();
//        edittext_name=(EditText)findViewById(R.id.edittext_name);
//        edittext_phone=(EditText)findViewById(R.id.edittext_phone);
//        edittext_email=(EditText)findViewById(R.id.edittext_email);
//        edittext_address=(EditText)findViewById(R.id.edittext_address);
//        edittext_im=(EditText)findViewById(R.id.edittext_im);
//        imagebutton=(ImageButton)findViewById(R.id.confinish_imgbut);
        
     //   MyButtonClickListener clickListener = new MyButtonClickListener();   
        
     //   imagebutton.setOnClickListener(clickListener); 
        

        Intent intent = getIntent();
        Bundle bundle=intent.getExtras();
        ContactInfo contactinfo= (ContactInfo)bundle.get("ContactInfo1");
        String str=Integer.toString(contactinfo.getId());
        name=contactinfo.getName();
//        Intent intent=getIntent();//getIntent将该项目中包含的原始intent检索出来，将检索出来的intent赋值给一个Intent类型的变量intent  
//        Bundle bundle=intent.getExtras();//.getExtras()得到intent所附带的额外数据  
//        //int str=Integer.parseInt(bundle.getString("str"));//getString()返回指定key的值  
//        String str=bundle.getString("str");
        
    
        cts = new ContactService(this );
        ContactInfo ct=new ContactInfo();
        ct=cts.getContactById(Integer.parseInt(str), name);
        
        name=ct.getName();
        phone=ct.getPhone();
        
        // 获取该联系人邮箱  
//        Cursor emails = getContentResolver().query(  ContactsContract.CommonDataKinds.Email.CONTENT_URI,  null, 
//               ContactsContract.CommonDataKinds.Phone.CONTACT_ID  + " = " + str, null, null);  
//        if (emails!=null)
//        {
//        	if (emails.moveToFirst()) {  
//                do {  
//                    // 遍历所有的电话号码  
//                    String emailType = emails .getString(emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));  
//                    String emailValue = emails .getString(emails .getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));                      
//                    if(Integer.parseInt(emailType)==2||Integer.parseInt(emailType)==1)
//                    {
//                    	companyemail=emailValue;
//                    }
//                    	
//                    Log.i("emailType", emailType);  
//                    Log.i("emailValue", emailValue);  
//                } while (emails.moveToNext());  
//            }       	
//        }
//        
//        emails.close();
//        
//     // 获取该联系人IM  
//        Cursor IMs = getContentResolver().query(  
//                Data.CONTENT_URI,  
//                new String[] { Data._ID, Im.PROTOCOL, Im.DATA },  
//                Data.CONTACT_ID + "=?" + " AND " + Data.MIMETYPE + "='"  
//                        + Im.CONTENT_ITEM_TYPE + "'",  
//                new String[] { str }, null);  
//        if(IMs!=null)
//        {
//        	if (IMs.moveToFirst()) {  
//                do {  
//                    String protocol = IMs.getString(IMs  
//                            .getColumnIndex(Im.PROTOCOL));  
//                    String date = IMs  
//                            .getString(IMs.getColumnIndex(Im.DATA));  
//                    if(Integer.parseInt(protocol)==4)  //如果类型为QQ
//                    {
//                    	QQ=date;
//                    }
////                    Log.i("protocol", protocol);  
////                    Log.i("date", date);  
//                } while (IMs.moveToNext());  
//            }  	
//        }
//        IMs.close();
//        
//        
//        // 获取该联系人地址  
//        Cursor address = getContentResolver()  
//                .query(  
//                        ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI,  
//                        null,  
//                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID  
//                                + " = " + str, null, null);  
//        
//        if(address!=null)
//        {
//        	if (address.moveToFirst()) {  
//                do {  
//                    // 遍历所有的地址  
//                    String street = address  
//                            .getString(address  
//                                    .getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.STREET));  
//                    String city = address  
//                            .getString(address  
//                                    .getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.CITY));  
//                    String region = address  
//                            .getString(address  
//                                    .getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.REGION));  
//                    String postCode = address  
//                            .getString(address  
//                                    .getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE));  
//                    String formatAddress = address  
//                            .getString(address  
//                                    .getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS));  
//
//                    personstreet=street;
//                    //                Log.i("street", street);  
////                    Log.i("city", city);  
////                    Log.i("region", region);  
////                    Log.i("postCode", postCode);  
////                    Log.i("formatAddress", formatAddress);  
//                } while (address.moveToNext());  
//            }  
//        	
//        }
//        address.close();
//        
//        
//        edittext_name.setText(name);
//        edittext_phone.setText(phone);
//        edittext_email.setText(companyemail);
//        edittext_address.setText(personstreet);
//        edittext_im.setText(QQ);
//            
//	}
//	
//	
//	 private void updateContact(String oldname) {
//
//		 Cursor cursor = getContentResolver().query(Data.CONTENT_URI,new String[] { Data.RAW_CONTACT_ID },
//
//		 ContactsContract.Contacts.DISPLAY_NAME + "=?",new String[] { oldname }, null);
//		 cursor.moveToFirst();
//		 String id = cursor.getString(cursor.getColumnIndex(Data.RAW_CONTACT_ID));
//		 cursor.close();
//		 ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
//	
//		 ops.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
//				 .withSelection(
//
//				 Data.RAW_CONTACT_ID + "=?" + " AND "+ ContactsContract.Data.MIMETYPE + " = ?" +
//				 " AND " + Phone.TYPE + "=?",new String[] { String.valueOf(id),Phone.CONTENT_ITEM_TYPE,
//				 String.valueOf(Phone.TYPE_MOBILE) }).withValue(Phone.NUMBER, edittext_phone.getText().toString()).build());
//		 
//		 		
//
//				 // 更新email
//				 ops.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
//				 .withSelection(Data.RAW_CONTACT_ID + "=?" + " AND "+ ContactsContract.Data.MIMETYPE + " = ?" +" AND " + Email.TYPE + "=?",new String[] { String.valueOf(id),Email.CONTENT_ITEM_TYPE,
//				 String.valueOf(Email.TYPE_HOME) }).withValue(Email.DATA, edittext_email.getText().toString()).build());
//
//				// 更新im
//				 ops.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
//				 .withSelection(Data.RAW_CONTACT_ID + "=?" + " AND "+ ContactsContract.Data.MIMETYPE + " = ?" +" AND " + Im.TYPE + "=?",new String[] { String.valueOf(id),Im.CONTENT_ITEM_TYPE,
//				 String.valueOf(Im.PROTOCOL_QQ) }).withValue(Im.DATA, edittext_im.getText().toString()).build());
//				 
//				// 更新address
//				 ops.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
//				 .withSelection(Data.RAW_CONTACT_ID + "=?" + " AND "+ ContactsContract.Data.MIMETYPE + " = ?" +" AND " + StructuredPostal.TYPE + "=?",new String[] { String.valueOf(id),StructuredPostal.CONTENT_ITEM_TYPE,
//				 String.valueOf(StructuredPostal.TYPE_HOME) }).withValue(Im.DATA, edittext_address.getText().toString()).build());
//				 
//				 // 更新姓名
//				 ops.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
//				 .withSelection(Data.RAW_CONTACT_ID + "=?" + " AND "+ ContactsContract.Data.MIMETYPE + " = ?",new String[] { String.valueOf(id),StructuredName.CONTENT_ITEM_TYPE }).withValue(StructuredName.DISPLAY_NAME, edittext_name.getText().toString()).build());
//
//				 // 更新网站
////				 ops.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
////				 .withSelection(Data.RAW_CONTACT_ID + "=?" + " AND "+ ContactsContract.Data.MIMETYPE + " = ?",new String[] { String.valueOf(id),Website.CONTENT_ITEM_TYPE }).withValue(Website.URL, website).build());
//
//				 // 更新公司
////				 ops.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
////				 .withSelection(Data.RAW_CONTACT_ID + "=?" + " AND "+ ContactsContract.Data.MIMETYPE + " = ?",new String[] { String.valueOf(id),Organization.CONTENT_ITEM_TYPE })
////				 .withValue(Organization.COMPANY, organization).build());
//
//				 // 更新note
////				 ops.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
////				 .withSelection(Data.RAW_CONTACT_ID + "=?" + " AND "+ ContactsContract.Data.MIMETYPE + " = ?",new String[] { String.valueOf(id),Note.CONTENT_ITEM_TYPE }).withValue(Note.NOTE, note).build());
//
//				 try{
//				 getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
//				 } catch (Exception e) {
//				 }
//				 }
//	
//	 class MyButtonClickListener implements OnClickListener   
//	    {  
//	        public void onClick(View v)   
//	        {  
//	        	String oldname="";
//	        	oldname=edittext_name.getText().toString();
//	        	try{
//	        		updateContact(oldname);	
//	        		ContactEditActivity.this.finish();  //添加成功后关闭当前activity，即返回上一界面  
//	        	}
//	        	catch (Exception e){
//	        		Toast.makeText(ContactEditActivity.this,
//		    				"编辑失败 ", Toast.LENGTH_SHORT)
//		    				.show();
//	        	}
//	        	Toast.makeText(ContactEditActivity.this,
//	    				"编辑成功 ", Toast.LENGTH_SHORT)
//	    				.show();
//	        	ContactEditActivity.this.finish();
//	        }  
//	    }  
	 
	

}
}
