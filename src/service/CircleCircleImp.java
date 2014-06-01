package service;

import java.util.List;

import android.graphics.Bitmap;
import android.net.Uri;
import entity.CallInfo;
import entity.ContactInfo;
import entity.ConversationInfo;
import entity.SmsInfo;

public class CircleCircleImp implements CircleCircleFacade{
	private CallService callService;
	private ContactService contactService;
	private SmsService smsService;
	private ConvService convService;
	
	public CircleCircleImp(){
		callService = new CallService();
		contactService = new ContactService();
		smsService = new SmsService();
		convService = new ConvService();
	}
	@Override
	public List<CallInfo> getCallLog() {
		// TODO Auto-generated method stub
		return this.callService.getCallLog();
	}

	@Override
	public int deleteCall(String phone) {
		// TODO Auto-generated method stub
		return this.callService.deleteCall(phone);
	}

	@Override
	public List<ContactInfo> getContactInfo() {
		// TODO Auto-generated method stub
		return this.contactService.getContactInfo();
	}

	@Override
	public List<ContactInfo> getContactInfoWithoutGroup() {
		// TODO Auto-generated method stub
		return this.contactService.getContactInfoWithoutGroup();
	}

	@Override
	public ContactInfo getContactById(int id, String cname) {
		// TODO Auto-generated method stub
		return this.contactService.getContactById(id, cname);
	}

	@Override
	public Bitmap getConvPhotoByPhone(String phone) {
		// TODO Auto-generated method stub
		return this.contactService.getConvPhotoByPhone(phone);
	}

	@Override
	public String getContactNameByPhone(String phone) {
		// TODO Auto-generated method stub
		return this.contactService.getContactNameByPhone(phone);
	}

	@Override
	public List<ConversationInfo> getConvInfo() {
		// TODO Auto-generated method stub
		return this.convService.getConvInfo();
	}

	@Override
	public String getUnreadNum() {
		// TODO Auto-generated method stub
		return this.convService.getUnreadNum();
	}

	@Override
	public String[] getCanonicalAddressById(String recipientId) {
		// TODO Auto-generated method stub
		return this.convService.getCanonicalAddressById(recipientId);
	}

	@Override
	public int deleteConversation(long id) {
		// TODO Auto-generated method stub
		return this.convService.deleteConversation(id);
	}

	@Override
	public List<SmsInfo> getSmsByConvId(long Convid) {
		// TODO Auto-generated method stub
		return this.smsService.getSmsByConvId(Convid);
	}

	@Override
	public Uri addSms(SmsInfo sms) {
		// TODO Auto-generated method stub
		return this.smsService.addSms(sms);
	}

	@Override
	public int update(String uri) {
		// TODO Auto-generated method stub
		return this.smsService.update(uri);
	}

	@Override
	public int deleteSms(long l) {
		// TODO Auto-generated method stub
		return this.smsService.deleteSms(l);
	}

	@Override
	public void markUnread(long Convid) {
		// TODO Auto-generated method stub
		this.smsService.markUnread(Convid);
	}
	
}
