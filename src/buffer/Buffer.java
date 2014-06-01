package buffer;

import java.util.HashMap;
import java.util.Map;

import utils.ContextUtil;

import android.app.Activity;

public class Buffer {
	protected ContextUtil activity;
	protected Map hashBuffer;
	public Buffer(){
		this.activity = ContextUtil.getInstance();
	}
}
	