package utils;

import entity.ContactInfo;
import entity.ConversationInfo;

public class ConvNameFormat {
	
	public static String ConvNameFormat(ContactInfo[] ctis){
		return formatConvName(ctis);
	}
	
	public static String formatConvName(ContactInfo[] ctis){
		int i;
		String name = "";
		for (i = 0; i < ctis.length - 1; i++) {
			if(ctis[i].getName() != null){
				name += ctis[i].getName() + ",";
			}else{
				name += ctis[i].getPhone() + ",";
			}
		}
		if(ctis[i].getName() != null){
			name += ctis[i].getName();
		}else{
			name += ctis[i].getPhone();
		}
		
		return name;
		
	}
}
