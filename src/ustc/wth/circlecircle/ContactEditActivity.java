package ustc.wth.circlecircle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import entity.ContactInfo;
import entity.GroupInfo;
import service.ContactService;
import adapter.EditGroupadapter;
import adapter.GroupsListAdapter;
import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.GroupMembership;
import android.provider.ContactsContract.CommonDataKinds.Im;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.CommonDataKinds.StructuredPostal;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ContactEditActivity extends Activity{
	private EditText edittext_name,edittext_phone,edittext_email,edittext_address,edittext_im;
	private TextView textview_group;
	private ImageButton imagebutton;
	private ContactService cts;
	private EditGroupadapter groupAdapter;
	private List<GroupInfo> groupinfos;
	private Spinner mSpinner; 
	private int flag=0;
	private HashMap<String, String> contactgroup;
    String companyemail="";
    String QQ="";
    String personstreet="";
    String name="";
    String phone="";
	
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_edit);
        

        flag=0;
        
        edittext_name=(EditText)findViewById(R.id.edittext_name);
        edittext_phone=(EditText)findViewById(R.id.edittext_phone);
        edittext_email=(EditText)findViewById(R.id.edittext_email);
        edittext_address=(EditText)findViewById(R.id.edittext_address);
        edittext_im=(EditText)findViewById(R.id.edittext_im);
        imagebutton=(ImageButton)findViewById(R.id.confinish_imgbut);
        textview_group=(TextView)findViewById(R.id.textview_editgroup);
        
        MyButtonClickListener clickListener = new MyButtonClickListener();   
        
        imagebutton.setOnClickListener(clickListener); 
        

        Intent intent = getIntent();
        Bundle bundle=intent.getExtras();
        final ContactInfo contactinfo= (ContactInfo)bundle.get("ContactInfo1");
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
        Cursor emails = getContentResolver().query(  ContactsContract.CommonDataKinds.Email.CONTENT_URI,  null, 
               ContactsContract.CommonDataKinds.Email.RAW_CONTACT_ID  + " = " + str, null, null);  
        if (emails!=null)
        {
        	if (emails.moveToFirst()) {  
                do {  
                    // 遍历所有的电话号码  
                    String emailType = emails .getString(emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));  
                    String emailValue = emails .getString(emails .getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));                      
                    if(Integer.parseInt(emailType)==2||Integer.parseInt(emailType)==1)
                    {
                    	companyemail=emailValue;
                    }
                    	
                    Log.i("emailType", emailType);  
                    Log.i("emailValue", emailValue);  
                } while (emails.moveToNext());  
            }       	
        }
        
        emails.close();
        
        String im=edittext_im.getText().toString();
     // 获取该联系人IM  
        Cursor IMs = getContentResolver().query(  
                Data.CONTENT_URI,  
                new String[] { Data._ID, Im.PROTOCOL, Im.DATA },  
                Data.RAW_CONTACT_ID + "=?" + " AND " + Data.MIMETYPE + "='"  
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
                        ContactsContract.CommonDataKinds.Phone.RAW_CONTACT_ID  
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
        
        
        groupinfos = new ArrayList<GroupInfo>();
        contactgroup=new HashMap<String,String>();
        String[] RAW_PROJECTION = new String[] { ContactsContract.Groups._ID, ContactsContract.Groups.TITLE };  
        String RAW_CONTACTS_WHERE = ContactsContract.Groups.DELETED + " = ? ";  
        Cursor groupscursor = getContentResolver().query(ContactsContract.Groups.CONTENT_URI, RAW_PROJECTION,  
                RAW_CONTACTS_WHERE, new String[] { "" + 0 }, null);  
        if (groupscursor != null) {
			while (groupscursor.moveToNext()) {
					String title = groupscursor.getString(groupscursor.getColumnIndex("title"));
					String id=groupscursor.getString(groupscursor.getColumnIndex("_id"));
					GroupInfo groupentity=new GroupInfo();
					groupentity.setGroupid(id);
					groupentity.setName(title);
					groupinfos.add(groupentity);
					contactgroup.put(id,title);
				}
			}
        groupscursor.close();
        
        
        
        
        Cursor groupcursor=null;
        String[] groups= new String[]{GroupMembership.GROUP_ROW_ID};
        String groupid="";
        String groupname="";
        String where=GroupMembership.RAW_CONTACT_ID+" = ?"+" AND " +Data.MIMETYPE + "='" + GroupMembership.CONTENT_ITEM_TYPE+"'";
        groupcursor=getContentResolver().query(Data.CONTENT_URI, groups, where, new String[]{str}, null);
        int eur=groupcursor.getCount();

        while (groupcursor.moveToNext()) { 
        	//Log.i("123", "groupcursor："+groupcursor.getCount());
        	groupid=groupcursor.getString(groupcursor.getColumnIndex(GroupMembership.GROUP_ROW_ID));
          	//  Log.i(Integer.toString(cursor.getInt(id)), "查询分组结果："+groupid);
        	groupname+=contactgroup.get(groupid);
        	groupname+=",";
        }
        groupcursor.close();
        if(!groupname.equals(""))
        {
        	groupname=groupname.substring(0,groupname.length()-1);
        	Log.d("sswwwwww",groupname);
        }
        edittext_name.setText(name);
        edittext_phone.setText(phone);
        edittext_email.setText(companyemail);
        edittext_address.setText(personstreet);
        edittext_im.setText(QQ);
        textview_group.setText(groupname);
            
	}
	
	
	 private void updateContact(String oldname) {

		 Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");  
	     ContentValues values = new ContentValues(); 
	        
		 Cursor cursor = getContentResolver().query(Data.CONTENT_URI,new String[] { Data.RAW_CONTACT_ID },

		 ContactsContract.Contacts.DISPLAY_NAME + "=?",new String[] { oldname }, null);
		 
		 String newemail=edittext_email.getText().toString();
		 
		 cursor.moveToFirst();
		 String id = cursor.getString(cursor.getColumnIndex(Data.RAW_CONTACT_ID));
		 cursor.close();
		 ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
	
		 ops.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
				 .withSelection(

				 Data.RAW_CONTACT_ID + "=?" + " AND "+ ContactsContract.Data.MIMETYPE + " = ?" +
				 " AND " + Phone.TYPE + "=?",new String[] { id ,Phone.CONTENT_ITEM_TYPE,
				 String.valueOf(Phone.TYPE_MOBILE) }).withValue(Phone.NUMBER, edittext_phone.getText().toString()).build()); 

				 // 更新email
		 		
		 		 ops.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
							 .withSelection(Data.RAW_CONTACT_ID + "=?" + " AND "+ ContactsContract.Data.MIMETYPE + " = ?" +" AND " + Email.TYPE + "=?",new String[] { id,Email.CONTENT_ITEM_TYPE,
							 String.valueOf(Email.TYPE_HOME) }).withValue(Email.DATA, newemail).build());
		 		// 更新im

		 		 ops.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
		 					 .withSelection(Data.RAW_CONTACT_ID + "=?" + " AND "+ ContactsContract.Data.MIMETYPE + " = ?" +" AND " + Im.TYPE + "=?",new String[] { String.valueOf(id),Im.CONTENT_ITEM_TYPE,
		 					 String.valueOf(Im.PROTOCOL_QQ) }).withValue(Im.DATA, edittext_im.getText().toString()).build());	 			 			
		 
				// 更新address
				 if(personstreet.equals(""))
				 {
					 	  
			        values.clear();  
			        values.put(android.provider.ContactsContract.Contacts.Data.RAW_CONTACT_ID, id);  
			        values.put(Data.MIMETYPE, StructuredPostal.CONTENT_ITEM_TYPE); 
			        values.put(StructuredPostal.DATA1, edittext_address.getText().toString()); 
			        values.put(StructuredPostal.TYPE, String.valueOf(StructuredPostal.TYPE_HOME));  
			        getContentResolver().insert(android.provider.ContactsContract.Data.CONTENT_URI, values);  
				 }
				 else
				 {
					 ops.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
							 .withSelection(Data.RAW_CONTACT_ID + "=?" + " AND "+ ContactsContract.Data.MIMETYPE + " = ?" +" AND " + StructuredPostal.TYPE + "=?",new String[] { String.valueOf(id),StructuredPostal.CONTENT_ITEM_TYPE,
							 String.valueOf(StructuredPostal.TYPE_HOME) }).withValue(StructuredPostal.DATA1, edittext_address.getText().toString()).build());		  
				 }
				 
				 
				 // 更新姓名
				 ops.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
						 .withSelection(Data.RAW_CONTACT_ID + "=?" + " AND "+ ContactsContract.Data.MIMETYPE + " = ?",new String[] { id ,StructuredName.CONTENT_ITEM_TYPE }).withValue(StructuredName.DISPLAY_NAME, edittext_name.getText().toString()).build());
				 
				 // 更新网站
//				 ops.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
//				 .withSelection(Data.RAW_CONTACT_ID + "=?" + " AND "+ ContactsContract.Data.MIMETYPE + " = ?",new String[] { String.valueOf(id),Website.CONTENT_ITEM_TYPE }).withValue(Website.URL, website).build());

				 // 更新公司
//				 ops.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
//				 .withSelection(Data.RAW_CONTACT_ID + "=?" + " AND "+ ContactsContract.Data.MIMETYPE + " = ?",new String[] { String.valueOf(id),Organization.CONTENT_ITEM_TYPE })
//				 .withValue(Organization.COMPANY, organization).build());

				 // 更新note
//				 ops.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
//				 .withSelection(Data.RAW_CONTACT_ID + "=?" + " AND "+ ContactsContract.Data.MIMETYPE + " = ?",new String[] { String.valueOf(id),Note.CONTENT_ITEM_TYPE }).withValue(Note.NOTE, note).build());

				 try{
				 getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
				 } catch (Exception e) {
				 }
				 }
	
	 class MyButtonClickListener implements OnClickListener   
	    {  
	        public void onClick(View v)   
	        {  
	        	String oldname="";
	        	oldname=edittext_name.getText().toString();
	        	try{
	        		updateContact(oldname);	
	        		ContactEditActivity.this.finish();  //添加成功后关闭当前activity，即返回上一界面  
	        	}
	        	catch (Exception e){
	        		Toast.makeText(ContactEditActivity.this,
		    				"编辑失败 ", Toast.LENGTH_SHORT)
		    				.show();
	        	}
	        	Toast.makeText(ContactEditActivity.this,
	    				"编辑成功 ", Toast.LENGTH_SHORT)
	    				.show();
	        	ContactEditActivity.this.finish();
	        }  
	    }  
	 

}











