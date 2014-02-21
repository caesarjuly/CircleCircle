package ustc.wth.circlecircle;

import global.Uris;

import entity.SmsInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import service.SmsService;

import android.support.v4.app.ListFragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentMessage extends ListFragment { 
	private List<SmsInfo> infos;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_message, container, false);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Uri uri = Uri.parse(Uris.SMS_URI_ALL);
		SmsService sms = new SmsService(this.getActivity(), uri);
		infos = sms.getSmsInfo();
		setListAdapter(new SmsListAdapter(this.getActivity()));

	}
	
	class SmsListAdapter extends BaseAdapter {
		private LayoutInflater layoutinflater;
		private View myview;

		public SmsListAdapter(Context c) {
			layoutinflater = LayoutInflater.from(c);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return infos.size();
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
			//if (convertView == null) {
				myview = layoutinflater.inflate(R.layout.message_line, null);
			//}
			TextView body = (TextView) myview
					.findViewById(R.id.body);
			TextView name = (TextView) myview
					.findViewById(R.id.name);
			TextView date = (TextView) myview
					.findViewById(R.id.date);
			body.setText(infos.get(position).getSmsbody());
			name.setText(infos.get(position).getName());
			date.setText(infos.get(position).getDate());
			return myview;
		}

	}

	public void onListItemClick(ListView parent, View v, int position, long id) {
		Toast.makeText(getActivity(),
				"You have selected ", Toast.LENGTH_SHORT)
				.show();
	}

}