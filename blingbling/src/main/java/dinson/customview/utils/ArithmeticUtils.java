package dinson.customview.utils;

import java.util.Stack;
import java.util.StringTokenizer;


/**
 * 四则运算（逆波兰算法）
 */
public class ArithmeticUtils {
    /**
     * 简单四则运算
     *
     * @param equation 四则混合运算表达式（支持加减乘除和括号）
     * @return 结果
     */
    public static double simpleCalculate(String equation) {
        Stack<String> operatorStack = new Stack<>();//存放运算符的栈
        Stack<Double> result = new Stack<>();

        StringTokenizer tokenizer = new StringTokenizer(equation, "+-*/()", true);
        while (tokenizer.hasMoreTokens()) {
            String element = tokenizer.nextToken();//分解算式后的单个元素

            if (isNum(element)) {
                result.push(Double.parseDouble(element));
            } else if (isOperator(element)) {
                //当栈为空时，或者当"("时，或者当栈顶的元素是"("时，直接入栈
                if (operatorStack.isEmpty() || "(".equals(element) || "(".equals(operatorStack.peek())) {
                    operatorStack.push(element);
                    continue;
                } else if (")".equals(element)) {
                    String string;
                    while (!"(".equals(string = operatorStack.pop())) {
                        result.push(calculate(result.pop(), result.pop(), string));
                    }
                    continue;
                }
                //栈内优先级大的或者相同的依次出栈，然后元素入栈
                while (!operatorStack.isEmpty() && !compareOperator(element, operatorStack.peek())) {
                    result.push(calculate(result.pop(), result.pop(), operatorStack.pop()));
                }
                operatorStack.push(element);
            } else {
                return 0;//含有非法字符
            }
        }
        // 遍历完原始表达式后  将操作符栈内元素 全部添加至 逆波兰表达式list
        while (!operatorStack.isEmpty()) {
            result.push(calculate(result.pop(), result.pop(), operatorStack.pop()));
        }
        return result.pop();
    }

    // 具体计算方法
    private static double calculate(double s1, double s2, String s3) {
        switch (s3) {
            case "+":
                return s2 + s1;
            case "-":
                return s2 - s1;
            case "*":
                return s2 * s1;
            case "/":
                if (s1 == 0) return s2;
                return s2 / s1;
            default:
                return 0;
        }
    }

    //--------------------------------辅助方法--------------------------------------------

    /**
     * 是否是数字
     */
    private static boolean isNum(String str) {
        //这里使用正则表达式 验证是否是数字
        return str.matches("^\\d+(\\.\\d+)?$");
    }

    /**
     * 是否是操作符
     */
    private static boolean isOperator(String str) {
        return str.matches("[+\\-*/()]");
    }

    /**
     * 比较运算符的优先级
     */
    private static boolean compareOperator(String op1, String op2) {
        return getLevel(op1) > getLevel(op2);
    }

    /**
     * 获取 优先级
     */
    private static int getLevel(String str) {
        switch (str) {
            case "+":
            case "-":
                return 1;
            case "*":
            case "/":
                return 2;
            default:
                return -1;
        }
    }
}
