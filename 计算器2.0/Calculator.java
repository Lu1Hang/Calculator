import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.math.BigDecimal;
import java.math.BigInteger;

@SuppressWarnings(value = { "all" })
/**
 * ������ѧ�������Ľ��棬��Ϊ��ťע�������
 * @author OneLine
 *
 */
public class Calculator extends JFrame implements ActionListener {
	JTextField textShow, textAnswer;				//���������ı��� һ����ʾ���ʽ һ����ʾ��
	JButton keys[] = new JButton[25];				//������ť
	String KEYS[] = { "change", "^", "C", "��", "/", "sin", "7", "8", "9", "*", "cos", "4", "5", "6", "-", "n!", "1",
			"2", "3", "+", "(", ")", "0", ".", "=" };//���������ƴ洢��������
	private double resultNumber = 0.0;
	private boolean firstDigit = true; 			// true��ʾ �Ǳ��ʽ�ĵ�һλ
	private boolean operatorFlag = false; 		// �жϷ����Ƿ��ص� false ��ʾδ���ַ���λ�������Ӽ��˳���С����
	private boolean operatorFlag2 = false;		// �ж�sin cos tan �Ƿ���� false��ʾδ����
	private boolean pointFlag = false; 			// �ж�С�����Ƿ��γ��� false��ʾδ����
	private boolean pointFlag2 = false;			// С����֮��Ҫ������ false��ʾδ����
	private boolean numberFlag = false; 		// �жϷ���ǰ�Ƿ���ֹ����� ���ʽ���һλӦ��Ϊ����
	private int leftNum = 0, rightNum = 0; 		// �����ź������ż���
	private boolean hanshuFlag = false; 		// ��ʼ���Ϊ���ּ�����

	MyMath my = new MyMath();					//
	String value = null;

	/**
	 * ���������������
	 */
	Calculator() {
		JPanel keysPanel = new JPanel();						// ���ü������
		
		keysPanel.setLayout(new GridLayout(5, 5, 3, 3));		// �������Ϊ����״
		// �����а������η�������� ����������ʹ�С
		for (int i = 0; i < 25; i++) {
			keys[i] = new JButton(KEYS[i]);
			keysPanel.add(keys[i]);
			keys[i].setFont(new Font("����", Font.BOLD, 20));
		}
		keys[0].setFont(new Font("����", Font.BOLD, 15));
		keys[0].setForeground(Color.GRAY);			//�����ְ�������Ϊ��ɫ
		keys[5].setForeground(Color.GRAY);
		keys[10].setForeground(Color.GRAY);
		keys[15].setForeground(Color.GRAY);

		for (int i = 0; i < 25; i++)
			keys[i].addActionListener(this); 	// ���а����ļ��������Ǹ�������
		textShow = new JTextField(); 			// �½��ı������ ��Ϊ������ʽ�������
		textAnswer = new JTextField(); 			// �½��ı������ ��Ϊ�õ��𰸵������
		JPanel top = new JPanel(); 				// �½���� �������ı������һ��
		top.setLayout(new BorderLayout());
		top.add("North", textShow);
		top.add("Center", textAnswer);

		textShow.setEditable(false); 							// ��ֹ������ı�����ⲿ������Ϣ
		textShow.setBackground(Color.WHITE);					// �����ı��򱳾�Ϊ��ɫ
		textShow.setHorizontalAlignment(JTextField.RIGHT); 		// �����ı�����Ҷ�д��
		textShow.setFont(new Font("����", Font.BOLD, 50)); 		// �����ı��������ֵ�����ʹ�С
		textAnswer.setHorizontalAlignment(JTextField.RIGHT);	// �����ı�����Ҷ�д��
		textAnswer.setFont(new Font("����", Font.BOLD, 50));		// �����ı��������ֵ�����ʹ�С

		getContentPane().add("North", top);					 // ���ϲ����ı����������ڴ��嶥��
		getContentPane().add("Center", keysPanel); 		   	// �������������ڴ���
		setVisible(true);									//��������Ϊ�ɼ�
		validate();												
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	/**
	 * ������
	 */
	public void actionPerformed(ActionEvent e) {//��д�������¼�
		String str = e.getActionCommand();
		if (str.equals(KEYS[2])) {				//��������㰴ťʱ
			handleC();							//ִ���������
		} else if (str.equals(KEYS[3])) {		//������˸�ťʱ
			handleBackspace();					//ɾ�����һ���ַ�������һЩ�ж�
		} else if ("0123456789.".indexOf(str) >= 0) {	//��������ֻ���С����ʱ
			handleNumber(str);					//�ı���������Ϸ������ֻ���С����
		} else if (str.equals(KEYS[4]) || str.equals(KEYS[9]) || str.equals(KEYS[14]) || str.equals(KEYS[19])
				|| str.equals(KEYS[24]) || str.equals(KEYS[1])) {//������������ťʱ
			handleOperator(str);				//�ı��������������
		} else if (str.equals(KEYS[0])) {		//�����change��ťʱ
			handleChange();						//�ı�������Ĺ��ܺͰ�����ɫ ͬʱ�ı�������
		} else if (str.equals(KEYS[20]) || str.equals(KEYS[21])) {//��������Ű�ťʱ
			handleBracket(str);					//�жϱ��ʽ�����Ƿ�Ϸ� �����ı�����������
		} else
			handleOperator2(str);				//�����sin��cos��n!��ťʱ  �ı�����������Ӧ����
	}
/**
 * ����ת�����水ť
 */
	private void handleChange() {
		handleC();								//�����������
		if (hanshuFlag == false) {				//�жϵ�ǰ״��Ϊ �������������
			hanshuFlag = true;					//�ı�� ����������
			keys[0].setForeground(Color.BLACK);//���ò��ְ�������ɫ
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
			hanshuFlag = false;					//�ı�� �������������
			keys[0].setForeground(Color.GRAY);	//���ò��ְ�������ɫ
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
	 * �������㽨
	 */
	private void handleC() {
		textShow.setText(" ");								//���ʽ���
		textShow.setFont(new Font("����", Font.BOLD, 40)); 	//�����ı��������ʹ�С
		textAnswer.setText("0");							//���������
		textAnswer.setFont(new Font("����", Font.BOLD, 40)); // �����ı��������ʹ�С
		firstDigit = true;									// ȫ������				
		operatorFlag = false; 
		operatorFlag2 = false;
		pointFlag = false;
	    pointFlag2 = false;
		numberFlag = false;
		leftNum = 0;
		rightNum = 0;
	}

	/**
	 * �����˸��
	 */
	private void handleBackspace() {
		String text = textShow.getText();			//�õ����ʽ���ַ���
		int i = text.length();						//������ʽ�ĳ���
		int del = 1;								//�趨ɾ����һ���ַ�
		if (i > 1) {								//�жϱ��ʽ�ַ����ȴ���0
			String lastChar = text.substring(i - 1, i);		//�õ����һ���ַ�
			String lastChar2 = text.substring(i - 2, i - 1);//�õ������ڶ����ַ�
			if (lastChar.equals("*") || lastChar.equals("/") || lastChar.equals("+") || lastChar.equals("-")
					|| lastChar.equals("^")) {//������һ���ַ��������
				operatorFlag = false;			//��ʾ����δ���� ��Ҫ��������
				operatorFlag2 = true;			//�������sin cos ���������
			} else if (lastChar.equals("n") || lastChar.equals("s")) {//�����sin cos���������
				del = 3;						//ɾ�����ַ���Ϊ3
				operatorFlag = false;			//��ʾ�����������δ����
				operatorFlag2 = false;			//��ʾ���������δ����
			} else if (lastChar.equals(".")) {	//������һ���ַ���С����
				pointFlag = false;				//��ʾδ����С����
				numberFlag = true;				//��ʾ��������
			}
			else if (lastChar.equals("("))		//�����������
				leftNum = leftNum - 1;			//�����ŵļ���ֵ�ı�
			else if (lastChar.equals(")"))		//�����������
				rightNum = rightNum - 1;		//�����ŵļ���ֵ�ı�
			else {								//���һ���ַ�������
				numberFlag = true;				//��ʾ��������
				if (lastChar2.equals(".")) {	//��������ڶ����ַ���С����
					pointFlag2 = false;			//��ʾС�����δ��������
				}
				else if (lastChar2.equals("*") || lastChar2.equals("/") || lastChar2.equals("+") || lastChar2.equals("-")
						|| lastChar2.equals("^")) {//��������ڶ����ַ��������
					numberFlag = false;			//��ʾδ��������
					operatorFlag = true;		//���������
				}
			}
			text = text.substring(0, i - del);	//ɾ���ַ�
			if (text.length() == 0) {			//���ı������ַ�������Ϊ0ʱ
				textShow.setText("0");			//�ı�������ʾ 0 
				textShow.setFont(new Font("����", Font.BOLD, 40));//��������ʹ�С
				firstDigit = true;				//���ʽΪ��
			} else {
				textShow.setFont(new Font("����", Font.BOLD, 40));//��������ʹ�С
				textShow.setText(text);			//���ı�������ʾ�˸����ַ���
			}
		}
		else {									//���ʽ��ֻ��һ���ַ�����û���ַ�
			textShow.setText("");				// ��ʾ���ı���
			textShow.setFont(new Font("����", Font.BOLD, 40));
		}
	}

	/**
	 * �������ּ�
	 * 
	 * @param key
	 */
	private void handleNumber(String key) {
		if (firstDigit) {					//������ʽΪ��
			textShow.setText(key);			//ֱ�Ӽ�����ַ�
		} else if (!key.equals(".")) {		//���������
			textShow.setText(textShow.getText() + key);			//���������ַ�
			if (pointFlag2 == false && pointFlag == true)		//����Ѿ�����С����
				pointFlag2 = true;								// С������������
		} else {
			String text = textShow.getText();					//�����С�������
			int i = text.length();								//�õ��ı������ַ�������
			String lastChar = text.substring(i - 1, i);			//�õ����һ���ַ�
			if ((key.equals(".")) && (pointFlag == false) && (numberFlag == true) && (pointFlag2 == false)
					&& "0123456789".indexOf(lastChar) > 0) {//�ж�С�����ܷ����
				textShow.setText(textShow.getText() + ".");     //�ı���������С����
				pointFlag = true;								//����С����
			}
			
		}
		firstDigit = false;										//���ʽ��Ϊ��
		numberFlag = true;										//��������
		operatorFlag = false; 									// ��Ҫ����λ
		operatorFlag2 = false;									//δ���ֺ��������
	}

	/**
	 * ���·��ż� �Ӽ��˳� ����������� ����������ı��ж�ֵ �������ֵ��ĩβ������С��ȥ�� ����������� ֱ�ӽ������� ��������ֵ�����ж� ����������С��
	 * 
	 * @param operator ��ť��Ӧ�������ַ�
	 */
	public void handleOperator(String operator) {
		// ��ǰ�������Ϊ�������������
		if (hanshuFlag == false) {
			// ���ַ������Ƿ��� ���� ĩβ������С����
			if (operatorFlag == false && firstDigit == false && (pointFlag == pointFlag2)) {
				// �����µȺŰ�ť ���������������Ÿ������ʱ
				if (operator.equals("=") && leftNum == rightNum) {
					value = textShow.getText(); // �õ��ı����еı��ʽ
					String answer = my.getEventuate(value); // ����getEventuate���� �õ����ʽ��ֵ
					if ("EK".indexOf(answer) > 0) { // ������ص��ַ����а����쳣�ж�
						textShow.setText(answer); // �ı�����������ʽ�쳣
					} else { // ����ֵ������
						double t1 = Double.valueOf(answer); // ȥ��ĩβΪ0��С��
						long t2 = (long) t1;
						double tt = t2 - t1;
						if (tt == 0.0)
							textAnswer.setText(String.valueOf(t2));
						else
							textAnswer.setText(String.valueOf(t1));
						textAnswer.setFont(new Font("����", Font.BOLD, 40));
						firstDigit = true; // ���ʽΪ��
					}
				} else
					textShow.setText(textShow.getText() + operator); // ���������������� ���ı���������÷���
				textShow.setFont(new Font("����", Font.BOLD, 40));
				// pointFlag = true;
				numberFlag = false;
				operatorFlag = true;
				operatorFlag2 = false;
				pointFlag = false;
				pointFlag2 = false;
			}
		} else {// ��ǰ����Ǻ���������
			if (operator.equals("=")) { // ���µȺŰ�ť
				value = textShow.getText(); // �õ��ı����е��ַ���
				int i = value.length(); // �õ��ַ����ĳ���
				if (i > 3) { // �ַ������ȴ���3ʱ ������sin cos ����
					String hanshu = value.substring(0, 3); // �õ��ַ���ǰ�����ַ�
					if (hanshu.equals("sin")) { // �����sin���� ���ַ���ת��Ϊ���� ���任�ɻ����ƽ�������
						double shuzi = Double.valueOf(value.substring(3, i));
						double hudu = Math.toRadians(shuzi);
						double result = Math.sin(hudu);
						textAnswer.setText(String.valueOf(result));
					} else if (hanshu.equals("cos")) { // �����sin���� ���ַ���ת��Ϊ���� ���任�ɻ����ƽ�������
						double shuzi = Double.valueOf(value.substring(3, i));
						double hudu = Math.toRadians(shuzi);
						double result = Math.cos(hudu);
						textAnswer.setText(String.valueOf(result));// ���ı�����������
					} else {
						String jiecheng = value.substring(i - 1, i);// �õ����һ���ַ�
						if (jiecheng.equals("!")) { // �ж��Ƿ�Ϊ�׳�
							double shuzi = Double.valueOf(value.substring(0, i - 1));// ���ַ���ת��������
							long n = (long) shuzi; // �ж��Ƿ�Ϊ����
							double k = n - shuzi;
							if (k != 0) {
								JOptionPane.showMessageDialog(null, "������Ȼ��", "��������!��", JOptionPane.YES_NO_OPTION);
								handleC();
							} else {
								if (n < 10000) {
									BigInteger flat = new BigInteger("1"); // �ô����ͼ���׳�
									BigInteger num = new BigInteger("1");
									int h = 0;
									for (h = 0; h < n; h++) {
										flat = flat.multiply(num);
										num = num.add(new BigInteger("1"));
									}
									textAnswer.setText(String.valueOf(flat));// ������
								} else
									JOptionPane.showMessageDialog(null, "���ֹ����޷����㡣", "��������!��",
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
							JOptionPane.showMessageDialog(null, "������Ȼ��", "��������!��", JOptionPane.YES_NO_OPTION);
							handleC();
						} else {
							if (n < 10000) { // �����ֹ���ʱ �޷�����
								BigInteger flat = new BigInteger("1");
								BigInteger num = new BigInteger("1");
								int h = 0;
								for (h = 0; h < n; h++) {
									flat = flat.multiply(num);
									num = num.add(new BigInteger("1"));
								}
								textAnswer.setText(String.valueOf(flat));
							} else
								JOptionPane.showMessageDialog(null, "���ֹ����޷����㡣", "��������!��", JOptionPane.YES_NO_OPTION);
						}
					}
				}
			}
		}
	}

	/**
	 * ���� cos sin n!
	 * 
	 * @param operator
	 */
	private void handleOperator2(String operator) {
		if (hanshuFlag == true) {						//��ǰ����Ϊ����������ʱ
			if (operatorFlag2 == false) {				//δ����sin cos n! ���������
				if (operator.equals("n!") && numberFlag == true) {//���½׳˰�ťʱ �ж��Ƿ���ֹ�����
					textShow.setText(textShow.getText() + "!");
				} else if (!operator.equals("n!") && numberFlag == false) {//�ж�sin cosֵ ��sin cos����ǰ���ܳ�������
					if (firstDigit) {					//���ʽΪ��
						textShow.setText(operator);		//���������
					} else
						textShow.setText(textShow.getText() + operator);
					firstDigit = false;					//���ʽ��Ϊ��
				}
				operatorFlag2 = true;					//���ֺ��������
			}
			textShow.setFont(new Font("����", Font.BOLD, 40));
		}
	}

	/**
	 * �������ż� �ж� �� ��������
	 * 
	 * @param operator
	 */
	private void handleBracket(String operator) {
		if (hanshuFlag == false) {				//��ǰ����Ϊ�������������
			if (operator.equals("(")) {			//���������Ű�ť
				if (firstDigit) {				
					textShow.setText(operator); 
				} else if (numberFlag == false) { 
					textShow.setText(textShow.getText() + operator);
				}
				leftNum = leftNum + 1;			//�����ŵļ���ֵ����һ
				firstDigit = false;
			} else if (operator.equals(")")) {			//���������Ű�ť
				if (numberFlag == true && leftNum > rightNum) {		//�жϱ��ʽ�Ƿ�Ϸ�
					textShow.setText(textShow.getText() + operator);
					rightNum = rightNum + 1;				//�����ŵļ���ֵ����һ
					operatorFlag = false;					//���Խ��е�ֵ����
				}
				textShow.setFont(new Font("����", Font.BOLD, 40));

			}
		}
	}
}