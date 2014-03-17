package ustc.wth.circlecircle;

import service.ContactService;
import entity.ContactInfo;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.CommonDataKinds.Im;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

public class ContactInfoActivity extends Activity{
	private TextView textview_name,textview_phone,textview_email,textview_im,textview_address;
	private ContactService cts;	
	
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_info);
        
        textview_name=(TextView)findViewById(R.id.textview_name);
        textview_phone=(TextView)findViewById(R.id.basicinfo_phone);
        textview_email=(TextView)findViewById(R.id.basicinfo_email);
        textview_im=(TextView)findViewById(R.id.basicinfo_im);
        textview_address=(TextView)findViewById(R.id.basicinfo_address);
        
        String selfemail="";
        String QQ="";
        String personstreet="";
        String name="";
        String phone="";
        
        //得到传过来的ContactInfo对象
//        Intent intent=getIntent();
//        Bundle bundle=intent.getExtras();
//        String str=bundle.getString("contact_id");
        Intent intent = getIntent();
        Bundle bundle=intent.getExtras();
        ContactInfo contactinfo= (ContactInfo)bundle.get("ContactInfo1");
        String str=Integer.toString(contactinfo.getId());
        name=contactinfo.getName();
        cts = new ContactService(this );
        ContactInfo ct=new ContactInfo();
        ct=cts.getContactById(Integer.parseInt(str), name);
        phone=ct.getPhone();
        
     // 获取该联系人邮箱  
        Cursor emails = getContentResolver().query(  ContactsContract.CommonDataKinds.Email.CONTENT_URI,  null, 
               ContactsContract.CommonDataKinds.Phone.CONTACT_ID  + " = " + str, null, null);  
        if (emails!=null)
        {
        	if (emails.moveToFirst()) {  
                do {  
                    // 遍历所有的电话号码  
                    String emailType = emails .getString(emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));  
                    String emailValue = emails .getString(emails .getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));                      
                    if(Integer.parseInt(emailType)==2||Integer.parseInt(emailType)==1)
                    {
                    	selfemail=emailValue;
                    }
                    	
                    Log.i("emailType", emailType);  
                    Log.i("emailValue", emailValue);  
                } while (emails.moveToNext());  
            }       	
        }
        
        emails.close();
        
     // 获取该联系人IM  
        Cursor IMs = getContentResolver().query(  
                Data.CONTENT_URI,  
                new String[] { Data._ID, Im.PROTOCOL, Im.DATA },  
                Data.CONTACT_ID + "=?" + " AND " + Data.MIMETYPE + "='"  
                        + Im.CONTENT_ITEM_TYPE + "'",  
                new String[] { str }, null);  
        if(IMs!=null)
        {
        	if (IMs.moveToFirst()) {  
                do {  
                    String protocol = IMs.getString(IMs  
                            .getColumnIndex(Im.PROTOCOL));  
                    Log.i("protocol", protocol);  
                    String date="";
                    if(IMs.getString(IMs.getColumnIndex(Im.DATA))=="")
                    	QQ="";
//                    String date = IMs  
//                            .getString(IMs.getColumnIndex(Im.DATA));  
                    if((Integer.parseInt(protocol)==4))  //如果类型为QQ
                    {
                    	QQ=IMs.getString(IMs.getColumnIndex(Im.DATA)); 
                    }
//                    Log.i("protocol", protocol);  
//                    Log.i("date", date);  
                } while (IMs.moveToNext());  
            }  	
        }
        IMs.close();
        
        
        // 获取该联系人地址  
        Cursor address = getContentResolver()  
                .query(  
                        ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI,  
                        null,  
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID  
                                + " = " + str, null, null);  
        
        if(address!=null)
        {
        	if (address.moveToFirst()) {  
                do {  
                    // 遍历所有的地址  
                    String street = address  
                            .getString(address  
                                    .getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.STREET));  
                    String city = address  
                            .getString(address  
                                    .getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.CITY));  
                    String region = address  
                            .getString(address  
                                    .getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.REGION));  
                    String postCode = address  
                            .getString(address  
                                    .getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE));  
                    String formatAddress = address  
                            .getString(address  
                                    .getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS));  

                    personstreet=street;

                } while (address.moveToNext());  
            }  
        	
        }
        address.close();      
        
       textview_name.setText(name);
       textview_phone.setText(phone);
       textview_email.setText(selfemail);
       textview_im.setText(QQ);
       textview_address.setText(personstreet);
   

	}
}
