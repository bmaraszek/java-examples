package leetcode.examples.stack;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import static org.assertj.core.api.Assertions.assertThat;

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                                                                                                                    //
//  TODO: Study this example                                                                                          //
//  NOTE: This is not really a stack exmaple. More like a queue.                                                      //
//                                                                                                                    //
//  Given n pairs of brackets, write a function to generate all combinations of well-formed bracket                   //
//                                                                                                                    //
//  Input: n = 1 --> Output: ["()"]                                                                                   //
//  Input: n = 3 --> Output: ["((()))","(()())","(())()","()(())","()()()"]                                           //
//                                                                                                                    //
//  Only add open bracket if open < n                                                                                 //
//  Only add a closing bracket if closed < open                                                                       //
//  Valid if open == closed == n                                                                                      //
//                                                                                                                    //
//  We're going to do it recursively, and we're going to backtrack.                                                   //
//                                                                                                                    //
//  Time complexity is O(n) because we go through the input once                                                      //
//  Space complexity is O(n) because we put values on the stack                                                       //
//                                                                                                                    //
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class GenerateBrackets {

    private Stack<Character> stack = new Stack<>();
    private List<String> result = new ArrayList<>();

    public List<String> backtrack(int n) {
        backtrack(0, 0, n);
        // instead of passing result down the recursion tree, we can use a field
        return result;
    }

    public void backtrack(int openN, int closedN, int n) {
        if(openN == closedN && closedN == n) {
            Iterator<Character> iter = stack.iterator();
            String temp = "";
            while(iter.hasNext()) {
                temp = temp + iter.next();
            }
            result.add(temp);
        }
        if(openN < n) {
            stack.push('(');
            backtrack(openN + 1, closedN, n);
            stack.pop();
        }
        if(closedN < openN) {
            stack.push(')');
            backtrack(openN, closedN + 1, n);
            stack.pop();
        }
    }

    @Test
    public void test() {
        GenerateBrackets subject = new GenerateBrackets();
        assertThat(subject.backtrack(1)).containsExactlyInAnyOrder("()");
        subject = new GenerateBrackets();
        assertThat(subject.backtrack(3)).containsExactlyInAnyOrder("((()))", "(()())", "(())()", "()(())", "()()()");
    }
}
