package holder;
import android.widget.TextView;

public class GroupsHolder {
	int position;
	TextView groupsname;
	TextView groupmembercount;
	TextView groupimg;
	
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	public TextView getGroupsname() {
		return groupsname;
	}
	public void setGroupsname(TextView groupsname) {
		this.groupsname = groupsname;
	}
	public TextView getGroupmembercount() {
		return groupmembercount;
	}
	public void setGroupmembercount(TextView groupmembercount) {
		this.groupmembercount = groupmembercount;
	}
	public TextView getGroupimg() {
		return groupimg;
	}
	public void setGroupimg(TextView groupimg) {
		this.groupimg = groupimg;
	}

}
