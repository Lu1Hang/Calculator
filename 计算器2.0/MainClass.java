import javax.swing.JFrame;
@SuppressWarnings(value = { "all" })
/**
 * ������
 * @author OneLine
 *
 */
public class MainClass{
	public static void main(String args[]) {
		Calculator frame ;
		frame = new Calculator();			//����һ������
		frame.setTitle("��ѧ������");		//���ô�������
		frame.setBounds(100,100,500,500);	//���ô�������ʹ�С
	}
}