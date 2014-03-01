package async;

import java.util.HashMap;

import buffer.ContactBuffer;
import holder.ThreadHolder;
import service.ContactService;
import ustc.wth.circlecircle.R;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import entity.ContactInfo;
import entity.ThreadInfo;

public class ContactAsyncLoader extends AsyncTask<Integer, Void, ContactInfo[]>{
		private ContactService cts;
		private ThreadHolder holder;
		private int position;
		private ThreadInfo thread;
		private HashMap<String, ContactInfo> cb;
		Context ctx;

		public ContactAsyncLoader(Context context, ThreadHolder holder, int position, ThreadInfo thread, ContactBuffer cb) {
			this.holder = holder;
			this.position = position;
			this.ctx = context;
			this.thread = thread;
			this.cb = cb.getBuffer();
			cts = new ContactService((Activity) ctx);
		}

		@Override
		protected ContactInfo[] doInBackground(Integer... isMass) {
			ContactInfo[] ctis;
			String phone;
			if(isMass[0] == 0){
				ctis = new ContactInfo[1];
				phone = thread.getCti().getPhone();
				if(cb.get(phone) == null){
					ctis[0] = cts.getContactByPhone(phone);
					cb.put(phone, ctis[0]);
				}else{
					ctis[0] = cb.get(phone);
				}
				
			}else{
				ctis = thread.getCtis();
				for(int i=0; i<ctis.length;i++){
					phone = ctis[i].getPhone();
					if(cb.get(phone) == null){
						ctis[i] = cts.getContactByPhone(phone);
						cb.put(phone, ctis[i]);
					}else{
						ctis[i] = cb.get(phone);
					}
				}
			}
			
			return ctis;
		}

		@Override
		protected void onPostExecute(ContactInfo[] result) {

			super.onPostExecute(result);
			if (position == holder.getPosition()) {
				if(thread.getIsMass() == 0){
					if (result[0].getName() != null) {
						holder.getName().setText(result[0].getName());
						String sb = result[0].getName().substring(0, 1);
						holder.getImg().setText(sb);
						holder.getImg().setBackgroundColor(ctx.getResources()
								.getColor(R.color.lightgray));
					}
					if(result[0].getPhoto() != null){
						Drawable bd = new BitmapDrawable(ctx.getResources(),
								result[0].getPhoto());
						holder.getImg().setText(null);
						holder.getImg().setBackground(bd);
					}
					
				}else{
					String name = "";
					int i;
					for (i = 0; i < result.length - 1; i++) {
						if(result[i].getName() == null){
							name += result[i].getPhone() + ",";
						}else{
							name += result[i].getName() + ",";			
						}

					}
					if(result[i].getName() == null){
						name += result[i].getPhone();
					}else{
						name += result[i].getName();			
					}

					holder.getName().setText(name);
				}
				
				
			}
		}
}
