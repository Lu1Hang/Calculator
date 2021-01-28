import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.math.BigDecimal;
import java.math.BigInteger;

@SuppressWarnings(value = { "all" })
/**
 * 构建科学计算器的界面，并为按钮注册监视器
 * @author OneLine
 *
 */
public class Calculator extends JFrame implements ActionListener {
	JTextField textShow, textAnswer;				//创建两个文本框 一个显示表达式 一个显示答案
	JButton keys[] = new JButton[25];				//创建按钮
	String KEYS[] = { "change", "^", "C", "←", "/", "sin", "7", "8", "9", "*", "cos", "4", "5", "6", "-", "n!", "1",
			"2", "3", "+", "(", ")", "0", ".", "=" };//按键的名称存储在数组中
	private double resultNumber = 0.0;
	private boolean firstDigit = true; 			// true表示 是表达式的第一位
	private boolean operatorFlag = false; 		// 判断符号是否重叠 false 表示未出现符号位（包括加减乘除和小数点
	private boolean operatorFlag2 = false;		// 判断sin cos tan 是否出现 false表示未出现
	private boolean pointFlag = false; 			// 判断小数点是否多次出现 false表示未出现
	private boolean pointFlag2 = false;			// 小数点之后要有数字 false表示未出现
	private boolean numberFlag = false; 		// 判断符号前是否出现过数字 表达式最后一位应该为数字
	private int leftNum = 0, rightNum = 0; 		// 左括号和右括号计数
	private boolean hanshuFlag = false; 		// 初始面板为数字计算器

	MyMath my = new MyMath();					//
	String value = null;

	/**
	 * 计算器界面的设置
	 */
	Calculator() {
		JPanel keysPanel = new JPanel();						// 设置键盘面板
		
		keysPanel.setLayout(new GridLayout(5, 5, 3, 3));		// 控制面板为网格状
		// 将所有按键依次放入面板中 并设置字体和大小
		for (int i = 0; i < 25; i++) {
			keys[i] = new JButton(KEYS[i]);
			keysPanel.add(keys[i]);
			keys[i].setFont(new Font("宋体", Font.BOLD, 20));
		}
		keys[0].setFont(new Font("宋体", Font.BOLD, 15));
		keys[0].setForeground(Color.GRAY);			//将部分按键设置为灰色
		keys[5].setForeground(Color.GRAY);
		keys[10].setForeground(Color.GRAY);
		keys[15].setForeground(Color.GRAY);

		for (int i = 0; i < 25; i++)
			keys[i].addActionListener(this); 	// 所有按键的监视器都是该面板对象
		textShow = new JTextField(); 			// 新建文本框对象 作为输入算式的输入端
		textAnswer = new JTextField(); 			// 新建文本框对象 作为得到答案的输出端
		JPanel top = new JPanel(); 				// 新建面板 将两个文本框放在一起
		top.setLayout(new BorderLayout());
		top.add("North", textShow);
		top.add("Center", textAnswer);

		textShow.setEditable(false); 							// 禁止输入端文本框从外部输入信息
		textShow.setBackground(Color.WHITE);					// 设置文本框背景为白色
		textShow.setHorizontalAlignment(JTextField.RIGHT); 		// 控制文本框从右端写入
		textShow.setFont(new Font("宋体", Font.BOLD, 50)); 		// 控制文本框内数字的字体和大小
		textAnswer.setHorizontalAlignment(JTextField.RIGHT);	// 控制文本框从右端写入
		textAnswer.setFont(new Font("宋体", Font.BOLD, 50));		// 控制文本框内数字的字体和大小

		getContentPane().add("North", top);					 // 将合并的文本框面板放置在窗体顶部
		getContentPane().add("Center", keysPanel); 		   	// 将键盘面板放置在窗体
		setVisible(true);									//窗口设置为可见
		validate();												
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	/**
	 * 监视器
	 */
	public void actionPerformed(ActionEvent e) {//重写监视器事件
		String str = e.getActionCommand();
		if (str.equals(KEYS[2])) {				//当点击清零按钮时
			handleC();							//执行清零操作
		} else if (str.equals(KEYS[3])) {		//当点击退格按钮时
			handleBackspace();					//删除最后一个字符并进行一些判断
		} else if ("0123456789.".indexOf(str) >= 0) {	//当点击数字或是小数点时
			handleNumber(str);					//文本框中输入合法的数字或是小数点
		} else if (str.equals(KEYS[4]) || str.equals(KEYS[9]) || str.equals(KEYS[14]) || str.equals(KEYS[19])
				|| str.equals(KEYS[24]) || str.equals(KEYS[1])) {//当点击运算符按钮时
			handleOperator(str);				//文本框中输入运算符
		} else if (str.equals(KEYS[0])) {		//当点击change按钮时
			handleChange();						//改变计算器的功能和按键颜色 同时文本框清零
		} else if (str.equals(KEYS[20]) || str.equals(KEYS[21])) {//当点击括号按钮时
			handleBracket(str);					//判断表达式构成是否合法 并在文本框输入括号
		} else
			handleOperator2(str);				//当点击sin、cos和n!按钮时  文本框中输入相应符号
	}
/**
 * 按下转换界面按钮
 */
	private void handleChange() {
		handleC();								//进行清零操作
		if (hanshuFlag == false) {				//判断当前状况为 四则运算计算器
			hanshuFlag = true;					//改变成 函数计算器
			keys[0].setForeground(Color.BLACK);//设置部分按键的颜色
			keys[5].setForeground(Color.BLACK);
			keys[10].setForeground(Color.BLACK);
			keys[15].setForeground(Color.BLACK);
			keys[1].setForeground(Color.GRAY);
			keys[4].setForeground(Color.GRAY);
			keys[9].setForeground(Color.GRAY);
			keys[14].setForeground(Color.GRAY);
			keys[19].setForeground(Color.GRAY);
			keys[20].setForeground(Color.GRAY);
			keys[21].setForeground(Color.GRAY);
		} else {
			hanshuFlag = false;					//改变成 四则运算计算器
			keys[0].setForeground(Color.GRAY);	//设置部分按键的颜色
			keys[5].setForeground(Color.GRAY);
			keys[10].setForeground(Color.GRAY);
			keys[15].setForeground(Color.GRAY);
			keys[1].setForeground(Color.BLACK);
			keys[4].setForeground(Color.BLACK);
			keys[9].setForeground(Color.BLACK);
			keys[14].setForeground(Color.BLACK);
			keys[19].setForeground(Color.BLACK);
			keys[20].setForeground(Color.BLACK);
			keys[21].setForeground(Color.BLACK);
		}
	}

	/**
	 * 按下清零建
	 */
	private void handleC() {
		textShow.setText(" ");								//表达式清空
		textShow.setFont(new Font("宋体", Font.BOLD, 40)); 	//设置文本框的字体和大小
		textAnswer.setText("0");							//输出端清零
		textAnswer.setFont(new Font("宋体", Font.BOLD, 40)); // 设置文本框的字体和大小
		firstDigit = true;									// 全部重置				
		operatorFlag = false; 
		operatorFlag2 = false;
		pointFlag = false;
	    pointFlag2 = false;
		numberFlag = false;
		leftNum = 0;
		rightNum = 0;
	}

	/**
	 * 按下退格键
	 */
	private void handleBackspace() {
		String text = textShow.getText();			//得到表达式的字符串
		int i = text.length();						//计算表达式的长度
		int del = 1;								//设定删除以一个字符
		if (i > 1) {								//判断表达式字符长度大于0
			String lastChar = text.substring(i - 1, i);		//得到最后一个字符
			String lastChar2 = text.substring(i - 2, i - 1);//得到倒数第二个字符
			if (lastChar.equals("*") || lastChar.equals("/") || lastChar.equals("+") || lastChar.equals("-")
					|| lastChar.equals("^")) {//如果最后一个字符是运算符
				operatorFlag = false;			//表示符号未出现 需要添加运算符
				operatorFlag2 = true;			//可以添加sin cos 函数运算符
			} else if (lastChar.equals("n") || lastChar.equals("s")) {//如果是sin cos函数运算符
				del = 3;						//删除的字符数为3
				operatorFlag = false;			//表示四则运算符号未出现
				operatorFlag2 = false;			//表示函数运算符未出现
			} else if (lastChar.equals(".")) {	//如果最后一个字符是小数点
				pointFlag = false;				//表示未出现小数点
				numberFlag = true;				//表示出现数字
			}
			else if (lastChar.equals("("))		//如果是左括号
				leftNum = leftNum - 1;			//左括号的计数值改变
			else if (lastChar.equals(")"))		//如果是右括号
				rightNum = rightNum - 1;		//右括号的计数值改变
			else {								//最后一个字符是数字
				numberFlag = true;				//表示出现数字
				if (lastChar2.equals(".")) {	//如果倒数第二个字符是小数点
					pointFlag2 = false;			//表示小数点后未出现数字
				}
				else if (lastChar2.equals("*") || lastChar2.equals("/") || lastChar2.equals("+") || lastChar2.equals("-")
						|| lastChar2.equals("^")) {//如果倒数第二个字符是运算符
					numberFlag = false;			//表示未出现数字
					operatorFlag = true;		//出现运算符
				}
			}
			text = text.substring(0, i - del);	//删除字符
			if (text.length() == 0) {			//当文本框中字符串长度为0时
				textShow.setText("0");			//文本框中显示 0 
				textShow.setFont(new Font("宋体", Font.BOLD, 40));//设置字体和大小
				firstDigit = true;				//表达式为空
			} else {
				textShow.setFont(new Font("宋体", Font.BOLD, 40));//设置字体和大小
				textShow.setText(text);			//在文本框中显示退格后的字符串
			}
		}
		else {									//表达式中只有一个字符或者没有字符
			textShow.setText("");				// 显示空文本框
			textShow.setFont(new Font("宋体", Font.BOLD, 40));
		}
	}

	/**
	 * 按下数字键
	 * 
	 * @param key
	 */
	private void handleNumber(String key) {
		if (firstDigit) {					//如果表达式为空
			textShow.setText(key);			//直接加入改字符
		} else if (!key.equals(".")) {		//如果是数字
			textShow.setText(textShow.getText() + key);			//加入数字字符
			if (pointFlag2 == false && pointFlag == true)		//如果已经出现小数点
				pointFlag2 = true;								// 小数点后出现数字
		} else {
			String text = textShow.getText();					//如果是小数点符号
			int i = text.length();								//得到文本框中字符串长度
			String lastChar = text.substring(i - 1, i);			//得到最后一个字符
			if ((key.equals(".")) && (pointFlag == false) && (numberFlag == true) && (pointFlag2 == false)
					&& "0123456789".indexOf(lastChar) > 0) {//判断小数点能否存在
				textShow.setText(textShow.getText() + ".");     //文本框中输入小数点
				pointFlag = true;								//出现小数点
			}
			
		}
		firstDigit = false;										//表达式不为空
		numberFlag = true;										//出现数字
		operatorFlag = false; 									// 需要符号位
		operatorFlag2 = false;									//未出现函数运算符
	}

	/**
	 * 按下符号键 加减乘除 四则运算界面 输入运算符改变判断值 并将输出值的末尾的无用小数去掉 函数运算界面 直接进行运算 对特殊数值进行判断 并弹出警告小窗
	 * 
	 * @param operator 按钮对应的运算字符
	 */
	public void handleOperator(String operator) {
		// 当前计算界面为四则运算计算器
		if (hanshuFlag == false) {
			// 首字符不能是符号 并且 末尾不能是小数点
			if (operatorFlag == false && firstDigit == false && (pointFlag == pointFlag2)) {
				// 当按下等号按钮 且左括号与右括号个数相等时
				if (operator.equals("=") && leftNum == rightNum) {
					value = textShow.getText(); // 得到文本框中的表达式
					String answer = my.getEventuate(value); // 调用getEventuate方法 得到表达式的值
					if ("EK".indexOf(answer) > 0) { // 如果返回的字符串中包含异常判断
						textShow.setText(answer); // 文本框中输出表达式异常
					} else { // 返回值是数字
						double t1 = Double.valueOf(answer); // 去掉末尾为0的小数
						long t2 = (long) t1;
						double tt = t2 - t1;
						if (tt == 0.0)
							textAnswer.setText(String.valueOf(t2));
						else
							textAnswer.setText(String.valueOf(t1));
						textAnswer.setFont(new Font("宋体", Font.BOLD, 40));
						firstDigit = true; // 表达式为空
					}
				} else
					textShow.setText(textShow.getText() + operator); // 如果是四则运算符号 在文本框中输入该符号
				textShow.setFont(new Font("宋体", Font.BOLD, 40));
				// pointFlag = true;
				numberFlag = false;
				operatorFlag = true;
				operatorFlag2 = false;
				pointFlag = false;
				pointFlag2 = false;
			}
		} else {// 当前面板是函数计算器
			if (operator.equals("=")) { // 按下等号按钮
				value = textShow.getText(); // 得到文本框中的字符串
				int i = value.length(); // 得到字符串的长度
				if (i > 3) { // 字符串长度大于3时 可能是sin cos 函数
					String hanshu = value.substring(0, 3); // 得到字符串前三个字符
					if (hanshu.equals("sin")) { // 如果是sin函数 将字符串转化为数字 并变换成弧度制进行运算
						double shuzi = Double.valueOf(value.substring(3, i));
						double hudu = Math.toRadians(shuzi);
						double result = Math.sin(hudu);
						textAnswer.setText(String.valueOf(result));
					} else if (hanshu.equals("cos")) { // 如果是sin函数 将字符串转化为数字 并变换成弧度制进行运算
						double shuzi = Double.valueOf(value.substring(3, i));
						double hudu = Math.toRadians(shuzi);
						double result = Math.cos(hudu);
						textAnswer.setText(String.valueOf(result));// 在文本框中输出结果
					} else {
						String jiecheng = value.substring(i - 1, i);// 得到最后一个字符
						if (jiecheng.equals("!")) { // 判断是否为阶乘
							double shuzi = Double.valueOf(value.substring(0, i - 1));// 将字符串转换成数字
							long n = (long) shuzi; // 判断是否为整数
							double k = n - shuzi;
							if (k != 0) {
								JOptionPane.showMessageDialog(null, "不是自然数", "【出错啦!】", JOptionPane.YES_NO_OPTION);
								handleC();
							} else {
								if (n < 10000) {
									BigInteger flat = new BigInteger("1"); // 用大整型计算阶乘
									BigInteger num = new BigInteger("1");
									int h = 0;
									for (h = 0; h < n; h++) {
										flat = flat.multiply(num);
										num = num.add(new BigInteger("1"));
									}
									textAnswer.setText(String.valueOf(flat));// 输出结果
								} else
									JOptionPane.showMessageDialog(null, "数字过大，无法计算。", "【出错啦!】",
											JOptionPane.YES_NO_OPTION);
							}
						}
					}
				} else {
					String jiecheng2 = value.substring(i - 1, i);
					if (jiecheng2.equals("!")) {
						double shuzi = Double.valueOf(value.substring(0, i - 1));
						long n = (long) shuzi;
						double k = n - shuzi;
						if (k != 0) {
							JOptionPane.showMessageDialog(null, "不是自然数", "【出错啦!】", JOptionPane.YES_NO_OPTION);
							handleC();
						} else {
							if (n < 10000) { // 当数字过大时 无法计算
								BigInteger flat = new BigInteger("1");
								BigInteger num = new BigInteger("1");
								int h = 0;
								for (h = 0; h < n; h++) {
									flat = flat.multiply(num);
									num = num.add(new BigInteger("1"));
								}
								textAnswer.setText(String.valueOf(flat));
							} else
								JOptionPane.showMessageDialog(null, "数字过大，无法计算。", "【出错啦!】", JOptionPane.YES_NO_OPTION);
						}
					}
				}
			}
		}
	}

	/**
	 * 按下 cos sin n!
	 * 
	 * @param operator
	 */
	private void handleOperator2(String operator) {
		if (hanshuFlag == true) {						//当前界面为函数计算器时
			if (operatorFlag2 == false) {				//未出现sin cos n! 函数运算符
				if (operator.equals("n!") && numberFlag == true) {//按下阶乘按钮时 判断是否出现过数字
					textShow.setText(textShow.getText() + "!");
				} else if (!operator.equals("n!") && numberFlag == false) {//判断sin cos值 且sin cos函数前不能出现数字
					if (firstDigit) {					//表达式为空
						textShow.setText(operator);		//输入运算符
					} else
						textShow.setText(textShow.getText() + operator);
					firstDigit = false;					//表达式不为空
				}
				operatorFlag2 = true;					//出现函数运算符
			}
			textShow.setFont(new Font("宋体", Font.BOLD, 40));
		}
	}

	/**
	 * 按下括号键 判断 （ ）并计数
	 * 
	 * @param operator
	 */
	private void handleBracket(String operator) {
		if (hanshuFlag == false) {				//当前界面为四则运算计算器
			if (operator.equals("(")) {			//按下左括号按钮
				if (firstDigit) {				
					textShow.setText(operator); 
				} else if (numberFlag == false) { 
					textShow.setText(textShow.getText() + operator);
				}
				leftNum = leftNum + 1;			//左括号的计数值增加一
				firstDigit = false;
			} else if (operator.equals(")")) {			//按下右括号按钮
				if (numberFlag == true && leftNum > rightNum) {		//判断表达式是否合法
					textShow.setText(textShow.getText() + operator);
					rightNum = rightNum + 1;				//右括号的计数值增加一
					operatorFlag = false;					//可以进行等值运算
				}
				textShow.setFont(new Font("宋体", Font.BOLD, 40));

			}
		}
	}
}