import javax.swing.JFrame;
@SuppressWarnings(value = { "all" })
/**
 * 主函数
 * @author OneLine
 *
 */
public class MainClass{
	public static void main(String args[]) {
		Calculator frame ;
		frame = new Calculator();			//创建一个窗口
		frame.setTitle("科学计算器");		//设置窗口名称
		frame.setBounds(100,100,500,500);	//设置窗口坐标和大小
	}
}