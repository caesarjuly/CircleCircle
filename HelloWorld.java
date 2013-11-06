package my.database.imp;//包名的定义需要根据项目来定。一般是项目名+子系统名+功能模块名

/*
 * 类的解释说明  			 eg.演示作用
 * 创建者   				 eg.桂俊飞
 * 创建时间				 eg.2013.11.06
 * */
public class HelloWorld {//类名首字母大写

	/*
	 * @param hello   hello成员变量的解释
	 * 
	 * @param world   world成员变量的解释
	 */

	private String hello;//变量名小写
	private int world;

	
	/*
	 * 方法的解释，作用     (一些功能性的方法特别需要解释)
	 * @param id     id的解释
	 * @return       返回值的解释（如果有）
	 * */
	public int getInfor(int id) {//方法名首字母小写
		
		if (id == 0) {//不管什么情况下，这对大括号必须存在
			this.world = this.world + 2;//操作符特别是等号两边最好使用空格
		} else {
			this.world--;
		}
		return this.world;
	}
	


}
