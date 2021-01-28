import java.util.Stack;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Stack;
import java.math.BigDecimal;
@SuppressWarnings(value = { "all" })
/**
 * 计算表达式
 * @author OneLine
 *
 */
public class MyMath {
	//用栈存储运算符
    private Stack<Character> charStack = new Stack<Character>();
    //用栈存储数字
    private Stack<Double> doubStack = new Stack<Double>();
    // 若表达式存在错误 进行纠错
  private String Recount(String value) {
        char ch = 0;
        String reValue = "", newStr = "";
        int length = 0, index = 0, count = 0, i = 0;
        // 如果最后的那个数不是数字且也不是右括号，则截取掉
        if (value.length() > 0) {
            char last = value.charAt(value.length() - 1);
            if (!(last >= '0' && last <= '9') || last == '(') 
                value = value.substring(0, value.length() - 1);
        }
        // 修改(4+10-(6 ==>(4+10-(6))
        length = value.length();
        //逐一判断字符串里面的字符
        //缺少右括号的地方补上
        //为了后面的计算方便
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
                // 修改 10*(4(-5)8) ===> 10*(4*(-5)*8)
                if (index > 0)
                    if (newStr.charAt(index - 1) == ')')
                        reValue += "*";
                reValue += ch;
                if (index < length - 1) {
                    if (newStr.charAt(index + 1) == '(')
                        reValue += "*";
                }
            } else {
                // 修改 10-(5+5)(5+5) ===> 10-(5+5)*(5+5)
                if (index > 0)
                    if (ch == '(' && newStr.charAt(index - 1) == ')')
                        reValue += "*";
                // 针对 -(10/3)+3 ===> 0-(10/3)+3
                if (index == 0 && ch == '-')
                     reValue += "0";
                // 这里起到一个筛选掉其它字母的作用
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
        // 判断第一个是否为'-'号
        if(reValue.charAt(0) == '-'){
            reValue = "0" + reValue;
        }
        // 以等于号作为结束标记
        return (reValue + "=");
    }
/**
 * 设置每一个运算符的等级
 * @param ch 运算符
 * @return 运算符的等级
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
     * 两个运算符的优先级比较
     * @param One 第一个运算符
     * @param Two 第二个运算符
     * @return 返回两个优先级的关系
     */
    private char Judge(char One, char Two) {
    	//以字符数组定义优先级
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
        x = isSwitch(One);//调用isSwitch方法判断等级
        y = isSwitch(Two);
        return menu[x][y];
    }

	/**
	 * 利用栈计算表达式
	 * 
	 * @param reValue
	 * @return 以字符串表示表达式的值
	 */
	private String AddStacks(String reValue) {
		char nowaday = 0, nextPop = 0, ch = 0;
		int length = reValue.length(); // 字符串长度
		double number = 0, temp = 0, decimal = 1, result = 0, One = 0, Two = 0;
		boolean flag = false, sflag = false, dflag = false, nflag = false, reckon;
		// 栈中以等号作为开始标记
		charStack.push('=');
		int index = 0;
		while (index < length) {// 从第一个字符开始判断 直到最后一个字符
			ch = reValue.charAt(index);
			while (ch >= '0' && ch <= '9' || ch == '.') {// 如果是数字或是小数点
				if (ch != '.') {
					number = Double.parseDouble(String.valueOf(ch));
					temp = (temp * 10) + number; // 由字符合成数字
					flag = true; // 出现数字
					if (dflag) // 如果出现小数
						decimal *= 10; // 小数位数加一
				} else
					dflag = true; // 出现小数点 判断标志改变
				index++;
				ch = reValue.charAt(index); // 判断下一个字符
			}
			reckon = true; // 数字字符串结束
			if (flag == true) {
				// nflag 针对(-5+4) 的记录值
				// sflag 针对 -(5+4) 改为：(-5 + -4)
				if (nflag || sflag) {
					if (nflag)
						nflag = false;
					temp = -temp;
				}
				if (dflag) { // 出现小数点
					temp /= decimal; // 得到有小数的值
					dflag = false; // 恢复小数点判断标志
					decimal = 1; // 小数位数为0
				}
				doubStack.push(temp); // 将数字存入栈中
				temp = 0;
				flag = false;
			}
			if (sflag && ch == ')') // 如果出现右括号
				sflag = false;
			if (ch == '-') { // 如果是负号
				char chs = reValue.charAt(index - 1); // 得到负号前的字符
				char chn = reValue.charAt(index + 1); // 得到负号后的字符
				if (chs == '+' || chs == '-' || chs == '*' || chs == '/' || chs == '(') {
					// 如果类似于这种 5*-(10+4) 遇到-符号后面是减号则
					// 从-号后面的括号开始 依次转换为 5*(-10+-4)
					if (chn == '(') // 如果出现左括号
						sflag = true;
					else
						nflag = true; // 针对 + - * / 后面的 - ，作为负数来处理
					reckon = false;
				}
			}
			while (reckon) { // 下一个字符是运算符
				nowaday = reValue.charAt(index); // 得到表达式中下一个运算符
				nextPop = charStack.pop(); // 得到栈顶的运算符
				switch (Judge(nextPop, nowaday)) {// 比较两个运算符的优先级
				case '>': // 优先级高 先计算
					try {
						Two = doubStack.pop();
						One = doubStack.pop();
					} catch (Exception e) { // 存在不是数字的情况 捕捉异常
						return "ERROR";
					}
					switch (nextPop) { // 判断运算符号
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
					doubStack.push(result); // 将数字结果推入栈中
					break;
				case '<': // 优先级低 继续入栈
					charStack.push(nextPop);
					charStack.push(nowaday);
					reckon = false;
					break;
				case '=': // 到达表达式的最后 返回字符串结果
					// 保留二位小数点
					return (String.format("%g", doubStack.pop()));
				case 'K': // 左括号与右括号相撞 出栈的左括号不入栈并从右括号开始把符号往下移一位
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
 * 将表达式合法化 再进行计算
 * @param expression 表达式
 * @return 用字符串表示的表达式的值
 */
public String getEventuate(String expression) {
    return AddStacks(Recount(expression.trim()));
	}
}
	