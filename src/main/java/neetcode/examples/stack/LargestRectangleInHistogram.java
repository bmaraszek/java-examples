package neetcode.examples.stack;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Stack;

import static org.assertj.core.api.Assertions.assertThat;

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                                                                                                                    //
//  TODO: Hard exercise. Review this.                                                                                 //
//  Given an array of integers "heights" representing a histogram's bar height where the width of each bar is 1,      //
//  return the area of the largest rectangle in the histogram.                                                        //
//                                                                                                                    //
//  The brute force approach is to iterate over all the bars and try to extend them left and right.                   //
//  This is O(n^2) time complexity.                                                                                   //
//                                                                                                                    //
//  A better approach is to keep adding bards to a stack as long as they're monotonically increasing.                 //
//  When we encounter a bar that is lower than the previous one, calculate the size area the previous one:            //
//  area = (size of the stack) * (height of the last bar)                                                             //
//  and then pop it off the stack.                                                                                    //
//                                                                                                                    //
//  Time complexity is O(N) - We go through the array once                                                            //
//  Space complexity is O(N) - We allocate a stack of the size up to N.                                               //
//                                                                                                                    //
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class LargestRectangleInHistogram {

    @AllArgsConstructor
    public static class Bar {
        int startIdx;
        int height;
    }

    public static int solve(List<Integer> heights) {
        Stack<Bar> stack = new Stack<>();
        int maxArea = 0;
        int start;

        for(int i = 0; i < heights.size(); i++) {
            int currentHeight = heights.get(i);
            start = i;
            while(!stack.isEmpty() && stack.peek().height > currentHeight) {
                Bar previousBar = stack.pop();
                maxArea = Math.max(maxArea, previousBar.height * (i - previousBar.startIdx));
                start = previousBar.startIdx;
            }
            stack.push(new Bar(start, currentHeight));
        }

        while(!stack.isEmpty()) {
            Bar currentBar = stack.pop();
            maxArea = Math.max(maxArea, (currentBar.height * (heights.size() - currentBar.startIdx)));
        }

        return maxArea;
    }

    @Test
    public void test() {
        assertThat(solve(List.of())).isEqualTo(0);
        assertThat(solve(List.of(2, 4))).isEqualTo(4);
        assertThat(solve(List.of(2, 1, 5, 6, 2, 3))).isEqualTo(10);
        assertThat(solve(List.of(4, 4, 4, 4))).isEqualTo(16);
    }
}
