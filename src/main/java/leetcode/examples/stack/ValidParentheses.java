package leetcode.examples.stack;

import org.junit.jupiter.api.Test;

import java.util.LinkedList;

import static org.assertj.core.api.Assertions.assertThat;

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                                                                                                                    //
//  Given a string s containing just the characters (){}[] determine if it's valid.                                   //
//  - Every bracket is closed by the bracket of the same type in correct order                                        //
//                                                                                                                    //
//  We'll solve this using a stack. Every left bracket goes to the stack.                                             //
//  Every right bracket pops off the stack. Then we check if they match.                                              //
//                                                                                                                    //
//  Time complexity is O(N) - We go through each character once                                                       //
//  Space complexity is O(N) - Worst case is a stack with N elements                                                  //
//                                                                                                                    //
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class ValidParentheses {

    public static boolean isLeftBracket(char c) {
        if(c == '(' || c == '{' || c == '[') {
            return true;
        }
        return false;
    }

    public static boolean isMatch(char left, char right) {
        if(left == '(' && right == ')') return true;
        if(left == '{' && right == '}') return true;
        if(left == '[' && right == ']') return true;
        return false;
    }

    public static boolean validate(String s) {
        LinkedList<Character> stack = new LinkedList<>();
        for(char c : s.toCharArray()) {
            if(isLeftBracket(c)) {
                stack.push(c);
            } else {
                if(!stack.isEmpty() && isMatch(stack.peek(), c)) {
                    stack.pop();
                } else {
                    return false;
                }
            }
        }
        return stack.isEmpty();
    }

    @Test
    public void test() {
        assertThat(validate("({[][](}())")).isFalse();
        assertThat(validate("({[][]}())")).isTrue();
        assertThat(validate(")(")).isFalse();
        assertThat(validate("((([]))")).isFalse();
    }
}
