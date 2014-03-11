package ustc.wth.circlecircle;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentCenter extends Fragment{
	
	private LayoutInflater layoutinflater;
	private View myview;
	private Context context;


 	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {	

 		return inflater.inflate(R.layout.fragment_center, null);		
 	}	
  }
	
	
