package neetcode.examples.stack;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Stack;
import org.junit.jupiter.api.Test;

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                                                                                                                    //
//  Implement a MinStack class that supports push, pop, top, and min in constant time.                                //
//                                                                                                                    //
//  This can be done using 2 stacks. The minStack will hold the min value seen so far.                                //
//  When we push a value into the stack, we also push into the min stack a value that is the min of                   //
//  the current min and the top of the stack (minumum so far)                                                         //
//                                                                                                                    //
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class MinStack {

    Stack<Integer> stack = new Stack<>();
    Stack<Integer> minStack = new Stack<>();

    public void push(int value) {
        stack.push(value);
        // min stack may be empty, so we need to check it
        value = Math.min(value, minStack.isEmpty() ? value : minStack.peek());
        minStack.push(value);
    }

    public void pop() {
        stack.pop();
        minStack.pop();
    }

    public int top() {
        return stack.peek();
    }

    public int getMin() {
        return minStack.peek();
    }

    @Test
    public void test() {
        MinStack subject = new MinStack();
        subject.push(-2);
        subject.push(0);
        subject.push(-3);
        assertThat(subject.getMin()).isEqualTo(-3);
        subject.pop();
        assertThat(subject.top()).isEqualTo(0);
        assertThat(subject.getMin()).isEqualTo(-2);
    }
}
