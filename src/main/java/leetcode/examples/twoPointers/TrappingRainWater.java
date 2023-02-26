package leetcode.examples.twoPointers;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                                                                                                                    //
//  TODO: hard example; review it.                                                                                    //
//  Given N non-negative integers representing an elevation map where the width of each bar is 1,                     //
//  compute how much water it can trap after raining.                                                                 //
//                                                                                                                    //
//  For each index i, the amount of water that can be trapped there is min(maxLeft, maxRight) - heights[i]            //
//                                                                                                                    //
//  Solution 1: precalculate the max Left, max Right, and min(maxLeft, maxRight) in a table.                          //
//                                                                                                                    //
//  max left   | 0 | 0 | 1 | 1 | 2 | 2 | 2 | 2 | 3 | 3 | 3 | 3 |                                                      //
//  max right  | 3 | 3 | 3 | 3 | 3 | 3 | 3 | 2 | 2 | 2 | 1 | 0 |                                                      //
//  min(l, r)  | 0 | 0 | 1 | 1 | 2 | 2 | 2 | 2 | 2 | 2 | 1 | 0 |                                                      //
//                                                                                                                    //
//  Next, for each height in heights, calculate min(l, r) - heights[i] and sum them.                                  //
//                                                                                                                    //
//  Time complexity is O(N) - We do 4 passes through the array to precalculate the table                              //
//  Space complexity is O(N) - We used up to 3 lists to hold the intermediate results                                 //
//                                                                                                                    //
//  Solution 2:                                                                                                       //
//    - Declare 2 pointers - left and right                                                                           //
//    - Declare maxLeft and maxRight initially equal heights[left] and heights[right]                                 //
//    - Calculate the solution, and then move the pointer that has the smaller max value                              //
//    - Until left < right                                                                                            //
//                                                                                                                    //
//  Time complexity is O(N) - We pass the array once                                                                  //
//  Space coplexity is O(1) - We only hold some intermediate variables                                                //
//                                                                                                                    //
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class TrappingRainWater {

    public static int solve(List<Integer> heights) {
        if(heights.size() == 0) return 0;

        // These need to be arrays because they need a starting size and elems initialised to 0
        int[] maxLeft = new int[heights.size()];
        int[] maxRight = new int[heights.size()];
        int[] minLR = new int[heights.size()];

        // populate maxLeft
        maxLeft[0] = 0;
        for(int i = 1; i < heights.size(); i++) {
            maxLeft[i] = Math.max(maxLeft[i - 1], heights.get(i - 1));
        }

        // populate maxRight
        maxRight[heights.size() - 1] = 0;
        for(int i = heights.size() - 2; i >= 0; i--) {
            maxRight[i] = Math.max(maxRight[i + 1], heights.get(i + 1));
        }

        // populate minLR
        for(int i = 0; i < heights.size(); i++) {
            minLR[i] = Math.min(maxLeft[i], maxRight[i]);
        }

        // calculating the filan answer
        int accumulator = 0;

        for(int i = 0; i < heights.size(); i++) {
            int diff = minLR[i] - heights.get(i); // difference
            accumulator += diff < 0 ? 0 : diff; // we do not count negative numbers. Use 0 if negative.
        }

        return accumulator;
    }

    public static int solveConstSpace(List<Integer> heights) {
        if(heights.isEmpty()) return 0;

        int left = 0;
        int right = heights.size() - 1;
        int leftMax = heights.get(left);
        int rightMax = heights.get(right);
        int accumulator = 0;

        while(left < right) {
            if(leftMax <= rightMax) {
                left++;
                leftMax = Math.max(leftMax, heights.get(left));
                accumulator += leftMax - heights.get(left);
            } else {
                right--;
                rightMax = Math.max(rightMax, heights.get(right));
                accumulator += rightMax - heights.get(right);
            }
        }

        return accumulator;
    }

    @Test
    public void test() {
        assertThat(solve(List.of())).isEqualTo(0);
        assertThat(solve(List.of(0, 0, 0))).isEqualTo(0);
        assertThat(solve(List.of(0, 1, 0))).isEqualTo(0);
        assertThat(solve(List.of(2, 1, 2))).isEqualTo(1);
        assertThat(solve(List.of(0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1))).isEqualTo(6);
        assertThat(solve(List.of(4, 2, 0, 3, 2, 5))).isEqualTo(9);
    }

    @Test
    public void testConstSpace() {
        assertThat(solveConstSpace(List.of())).isEqualTo(0);
        assertThat(solveConstSpace(List.of(0, 0, 0))).isEqualTo(0);
        assertThat(solveConstSpace(List.of(0, 1, 0))).isEqualTo(0);
        assertThat(solveConstSpace(List.of(0, 0, 1, 0, 0))).isEqualTo(0);
        assertThat(solveConstSpace(List.of(2, 1, 2))).isEqualTo(1);
        assertThat(solveConstSpace(List.of(0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1))).isEqualTo(6);
        assertThat(solveConstSpace(List.of(4, 2, 0, 3, 2, 5))).isEqualTo(9);
    }
}
