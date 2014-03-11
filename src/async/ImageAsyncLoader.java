package async;

import java.util.HashMap;

import buffer.ContactBuffer;
import holder.ConversationHolder;
import service.ContactService;
import ustc.wth.circlecircle.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import entity.ContactInfo;


/**
 * 异步加载联系人的头像和号码
 * Date：2014-3-5
 * Author：王东东
 */
public class ImageAsyncLoader extends AsyncTask<Integer, Void, ContactInfo[]>{
		private ContactService cts;
		private ConversationHolder holder;
		private int position;
		private ContactInfo contact_info;
		private HashMap<String, ContactInfo> cm;
		Context ctx;
		private String phone_number;

		public ImageAsyncLoader(Context context, ConversationHolder holder, int position, ContactInfo contactinfo, ContactBuffer cb) {
			this.ctx=context;
			this.holder = holder;
			this.position = position;
			this.ctx = context;
			this.contact_info = contactinfo;
			this.cm = cb.getBuffer();
			cts = new ContactService((Activity) ctx);
		}

		@Override
		protected ContactInfo[] doInBackground(Integer... params) {
			ContactInfo[] ctis;
			String contactid;
			String al;
			String number;
			int id1;
				ctis = new ContactInfo[1];	//创建大小为1的ContactInfo[]
				//name = contact_info.getName();
				//获取id
				id1=contact_info.getId();
				al=contact_info.getName();
				contactid=Integer.toString(contact_info.getId());
				if(cm.get(contactid) == null){
					ctis[0] = cts.getContactById(Integer.parseInt(contactid),al);   //通过ContactService来获取ContacInfo对象
					al=ctis[0].getName();
					number=ctis[0].getPhone();
					cm.put(contactid, ctis[0]);        
					
				}else{
					ctis[0] = cm.get(contactid);
				}
				
				
			return ctis;
		}

		
		
		//在这里面可以使用在doInBackground 得到的结果处理操作UI
		@Override
		protected void onPostExecute(ContactInfo[] result) {

			super.onPostExecute(result);
			if (position == holder.getPosition()) {
	
					if (result[0].getPhone() != null) {
						phone_number=result[0].getPhone().trim();
						//为Img添加监听事件，点击头像可以进行
						holder.getImg().setOnClickListener(new OnClickListener() {
				            @Override
				            public void onClick(View v) {
				            	
				            	if(phone_number != null && !phone_number.equals(""))
				            	{
				                    //调用系统的拨号服务实现电话拨打功能
				                    //封装一个拨打电话的intent，并且将电话号码包装成一个Uri对象传入
				                    Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+phone_number));
				                    ctx.startActivity(intent);//内部类
				                }
				            	Toast.makeText(ctx, "正在拨号", Toast.LENGTH_SHORT).show();
				            }
				        });
						
						
						holder.getNum().setText(result[0].getPhone());
						String sb ="";
						//holder.getNum().setText("123");
						if(result[0].getName()==null)
							{
								sb="";		
								holder.getName().setText("我的名片");
							}
						else
						{
						sb = result[0].getName().substring(0, 1);//获取名字的第一个字符
						
						holder.getImg().setText(sb);
						
						holder.getImg().setBackgroundColor(ctx.getResources()
								.getColor(R.color.lightgray));
						}
					}
					if(result[0].getPhoto() != null){
						Drawable bd = new BitmapDrawable(ctx.getResources(),
								result[0].getPhoto());
						holder.getImg().setText(null);
						holder.getImg().setBackground(bd);
					}
					
			}
		}



}
