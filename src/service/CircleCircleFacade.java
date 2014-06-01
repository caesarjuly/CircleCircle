package service;

import java.util.List;

import android.graphics.Bitmap;
import android.net.Uri;

import entity.CallInfo;
import entity.ContactInfo;
import entity.ConversationInfo;
import entity.SmsInfo;

public interface CircleCircleFacade {
	List<CallInfo> getCallLog();

	int deleteCall(String phone);

	List<ContactInfo> getContactInfo();

	List<ContactInfo> getContactInfoWithoutGroup();

	ContactInfo getContactById(int id, String cname);

	Bitmap getConvPhotoByPhone(String phone);

	String getContactNameByPhone(String phone);

	List<ConversationInfo> getConvInfo();

	String getUnreadNum();

	String[] getCanonicalAddressById(String recipientId);

	int deleteConversation(long id);

	List<SmsInfo> getSmsByConvId(long Convid);

	Uri addSms(SmsInfo sms);

	int update(String uri);

	int deleteSms(long l);

	void markUnread(long Convid);

}
