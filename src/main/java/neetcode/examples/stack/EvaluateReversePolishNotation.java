package neetcode.examples.stack;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Stack;

import static org.assertj.core.api.Assertions.assertThat;

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                                                                                                                    //
//  Given an array of string tokens that represents an arithmetic expression in a Reverse Polish Notation,            //
//  evaluate the expression and return an integer that represents the value of the expression.                        //
//                                                                                                                    //
//  - Valid operators are +, -, *, /                                                                                  //
//  - Each operand may be an integer or another expression                                                            //
//  - Division between two integers always truncates toward zero                                                      //
//  - There will not be any division by zero                                                                          //
//  - The answers to all intermediate colculations can be represented as a 32-bit integer                             //
//                                                                                                                    //
//  Time complexity is O(n) because we go through the input once                                                      //
//  Space complexity is O(n) because we put values on the stack                                                       //
//                                                                                                                    //
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class EvaluateReversePolishNotation {

    public static int eval(List<String> expression) {
        Stack<Integer> stack = new Stack<>();
        for(int i = 0; i < expression.size(); i++) {
            String val = expression.get(i);
            if ("+".equals(val)) {
                stack.push(stack.pop() + stack.pop());
            } else if ("-".equals(val)) {
                Integer val2 = stack.pop();
                Integer val1 = stack.pop();
                stack.push(val1 - val2);
            } else if ("*".equals(val)) {
                stack.push(stack.pop() * stack.pop());
            } else if ("/".equals(val)) {
                Integer val2 = stack.pop();
                Integer val1 = stack.pop();
                stack.push(val1 / val2);
            } else {
                stack.push(Integer.valueOf(val));
            }
        }
        return stack.pop();
    }

    @Test
    public void test() {
        assertThat(eval(List.of("2", "1", "+", "3", "*"))).isEqualTo(9);
        assertThat(eval(List.of("4", "13", "5", "/", "+"))).isEqualTo(6);
        assertThat(eval(List.of("10", "6", "9", "3", "+", "-11", "*", "/", "*", "17", "+", "5", "+"))).isEqualTo(22);
    }
}
