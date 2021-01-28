import java.util.Stack;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Stack;
import java.math.BigDecimal;
@SuppressWarnings(value = { "all" })
/**
 * ������ʽ
 * @author OneLine
 *
 */
public class MyMath {
	//��ջ�洢�����
    private Stack<Character> charStack = new Stack<Character>();
    //��ջ�洢����
    private Stack<Double> doubStack = new Stack<Double>();
    // �����ʽ���ڴ��� ���о���
  private String Recount(String value) {
        char ch = 0;
        String reValue = "", newStr = "";
        int length = 0, index = 0, count = 0, i = 0;
        // ��������Ǹ�������������Ҳ���������ţ����ȡ��
        if (value.length() > 0) {
            char last = value.charAt(value.length() - 1);
            if (!(last >= '0' && last <= '9') || last == '(') 
                value = value.substring(0, value.length() - 1);
        }
        // �޸�(4+10-(6 ==>(4+10-(6))
        length = value.length();
        //��һ�ж��ַ���������ַ�
        //ȱ�������ŵĵط�����
        //Ϊ�˺���ļ��㷽��
        while (i < length) {
            ch = value.charAt(i);
            if (ch == '(') {
                count++;
            } else if (ch == ')') {
                count--;
            }
            if (ch >= '0' && ch <= '9') {
                newStr += ch;
            } else {
                if (count >= 0)
                    newStr += ch;
            }
            ++i;
        }
        while (count > 0) {
            newStr += ")";
            count--;
        }
        length = newStr.length();
        while (index != length) {
            ch = newStr.charAt(index);
            if (ch >= '0' && ch <= '9') {
                // �޸� 10*(4(-5)8) ===> 10*(4*(-5)*8)
                if (index > 0)
                    if (newStr.charAt(index - 1) == ')')
                        reValue += "*";
                reValue += ch;
                if (index < length - 1) {
                    if (newStr.charAt(index + 1) == '(')
                        reValue += "*";
                }
            } else {
                // �޸� 10-(5+5)(5+5) ===> 10-(5+5)*(5+5)
                if (index > 0)
                    if (ch == '(' && newStr.charAt(index - 1) == ')')
                        reValue += "*";
                // ��� -(10/3)+3 ===> 0-(10/3)+3
                if (index == 0 && ch == '-')
                     reValue += "0";
                // ������һ��ɸѡ��������ĸ������
                switch (ch) {
                case '+':
                case '-':
                case '*':
                case '/':
                case '(':
                case ')':
                case '.':
                case '^':
                    reValue += ch;
                    break;
                }
            }
            ++index;
        }
        // �жϵ�һ���Ƿ�Ϊ'-'��
        if(reValue.charAt(0) == '-'){
            reValue = "0" + reValue;
        }
        // �Ե��ں���Ϊ�������
        return (reValue + "=");
    }
/**
 * ����ÿһ��������ĵȼ�
 * @param ch �����
 * @return ������ĵȼ�
 */
    private int isSwitch(char ch) {
        int number = 0;
        switch (ch) {
        case '+': number = 0; break;
        case '-': number = 1; break;
        case '*': number = 2; break;
        case '/': number = 3; break;
        case '(': number = 4; break;
        case ')': number = 5; break;
        case '^': number = 6; break;
        case '=': number = 7; break;
        }
        return number;
    }
    /**
     * ��������������ȼ��Ƚ�
     * @param One ��һ�������
     * @param Two �ڶ��������
     * @return �����������ȼ��Ĺ�ϵ
     */
    private char Judge(char One, char Two) {
    	//���ַ����鶨�����ȼ�
        char[][] menu = { 
                { '>', '>', '<', '<', '<', '>', '<', '>' },
                { '>', '>', '<', '<', '<', '>', '<', '>' },
                { '>', '>', '>', '>', '<', '>', '<', '>' },
                { '>', '>', '>', '>', '<', '>', '<', '>' },
                { '<', '<', '<', '<', '<', 'K', '<', 'E' },
                { '<', '<', '<', '<', '<', '<', '<', 'E' },
                { '>', '>', '>', '>', '<', '>', '>', '>' },
                { '<', '<', '<', '<', '<', '<', '<', '=' }, };
        int x = 0, y = 0;
        x = isSwitch(One);//����isSwitch�����жϵȼ�
        y = isSwitch(Two);
        return menu[x][y];
    }

	/**
	 * ����ջ������ʽ
	 * 
	 * @param reValue
	 * @return ���ַ�����ʾ���ʽ��ֵ
	 */
	private String AddStacks(String reValue) {
		char nowaday = 0, nextPop = 0, ch = 0;
		int length = reValue.length(); // �ַ�������
		double number = 0, temp = 0, decimal = 1, result = 0, One = 0, Two = 0;
		boolean flag = false, sflag = false, dflag = false, nflag = false, reckon;
		// ջ���ԵȺ���Ϊ��ʼ���
		charStack.push('=');
		int index = 0;
		while (index < length) {// �ӵ�һ���ַ���ʼ�ж� ֱ�����һ���ַ�
			ch = reValue.charAt(index);
			while (ch >= '0' && ch <= '9' || ch == '.') {// ��������ֻ���С����
				if (ch != '.') {
					number = Double.parseDouble(String.valueOf(ch));
					temp = (temp * 10) + number; // ���ַ��ϳ�����
					flag = true; // ��������
					if (dflag) // �������С��
						decimal *= 10; // С��λ����һ
				} else
					dflag = true; // ����С���� �жϱ�־�ı�
				index++;
				ch = reValue.charAt(index); // �ж���һ���ַ�
			}
			reckon = true; // �����ַ�������
			if (flag == true) {
				// nflag ���(-5+4) �ļ�¼ֵ
				// sflag ��� -(5+4) ��Ϊ��(-5 + -4)
				if (nflag || sflag) {
					if (nflag)
						nflag = false;
					temp = -temp;
				}
				if (dflag) { // ����С����
					temp /= decimal; // �õ���С����ֵ
					dflag = false; // �ָ�С�����жϱ�־
					decimal = 1; // С��λ��Ϊ0
				}
				doubStack.push(temp); // �����ִ���ջ��
				temp = 0;
				flag = false;
			}
			if (sflag && ch == ')') // �������������
				sflag = false;
			if (ch == '-') { // ����Ǹ���
				char chs = reValue.charAt(index - 1); // �õ�����ǰ���ַ�
				char chn = reValue.charAt(index + 1); // �õ����ź���ַ�
				if (chs == '+' || chs == '-' || chs == '*' || chs == '/' || chs == '(') {
					// ������������� 5*-(10+4) ����-���ź����Ǽ�����
					// ��-�ź�������ſ�ʼ ����ת��Ϊ 5*(-10+-4)
					if (chn == '(') // �������������
						sflag = true;
					else
						nflag = true; // ��� + - * / ����� - ����Ϊ����������
					reckon = false;
				}
			}
			while (reckon) { // ��һ���ַ��������
				nowaday = reValue.charAt(index); // �õ����ʽ����һ�������
				nextPop = charStack.pop(); // �õ�ջ���������
				switch (Judge(nextPop, nowaday)) {// �Ƚ���������������ȼ�
				case '>': // ���ȼ��� �ȼ���
					try {
						Two = doubStack.pop();
						One = doubStack.pop();
					} catch (Exception e) { // ���ڲ������ֵ���� ��׽�쳣
						return "ERROR";
					}
					switch (nextPop) { // �ж��������
					case '+':
						result = One + Two;
						break;
					case '-':
						result = One - Two;
						break;
					case '*':
						result = One * Two;
						break;
					case '/':
						result = One / Two;
						break;
					case '^':
						result = Math.pow(One, Two);
						break;
					}
					doubStack.push(result); // �����ֽ������ջ��
					break;
				case '<': // ���ȼ��� ������ջ
					charStack.push(nextPop);
					charStack.push(nowaday);
					reckon = false;
					break;
				case '=': // ������ʽ����� �����ַ������
					// ������λС����
					return (String.format("%g", doubStack.pop()));
				case 'K': // ����������������ײ ��ջ�������Ų���ջ���������ſ�ʼ�ѷ���������һλ
					++index;
					break;
				case 'E':
					return ("ERROR");
				}
			}
			++index;
		}
		return null;
	}
 /**
 * �����ʽ�Ϸ��� �ٽ��м���
 * @param expression ���ʽ
 * @return ���ַ�����ʾ�ı��ʽ��ֵ
 */
public String getEventuate(String expression) {
    return AddStacks(Recount(expression.trim()));
	}
}
	