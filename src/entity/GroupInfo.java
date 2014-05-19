package entity;

import java.io.Serializable;

@SuppressWarnings("serial")
public class GroupInfo implements Serializable{

	private String name;

	private String groupid;
	
	private String groupmembercount;
	
	//private GroupInfo gpi;

	
//	public GroupInfo getGpi() {
//		return gpi;
//	}
//	
//	public void setGpi(GroupInfo gpi) {
//		this.gpi = gpi;
//	}
	
	public String getGroupid() {
		return groupid;
	}

	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getGroupmembercount() {
		return groupmembercount;
	}

	public void setGroupmembercount(String groupmembercount) {
		this.groupmembercount = groupmembercount;
	}
	
	
}
