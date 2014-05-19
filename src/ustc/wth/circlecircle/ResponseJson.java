package ustc.wth.circlecircle;


import java.io.Serializable;
import java.util.List;

public class ResponseJson implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ResponseJson(){}
	

	public String message;
	

	public Object data;
	
	/**
	 * 对象集合
	 */
	public List   dataEx;
	
	/**

	 */
	public String status;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public List getDataEx() {
		return dataEx;
	}

	public void setDataEx(List dataEx) {
		this.dataEx = dataEx;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	

}
