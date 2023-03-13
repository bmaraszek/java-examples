package neetcode.examples.binarySearch;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                                                                                                                    //
//  You are given an (m * n) integer matrix with the following 2 properties:                                          //
//   - Each row is sorted in non-decreasing order                                                                     //
//   - The first integer of each row is greater that the last integer of the previous row                             //
//                                                                                                                    //
//  Given an integer target, return true if target is in the matrix or false otherwise.                               //
//  The algorithm must be in O(log(m * n)) time complexity.                                                           //
//                                                                                                                    //
//  Time complexity is O(log m + log n) - A binary search twice                                                       //
//  Space complexity is O(1) - We only allocate variables for calculations                                            //
//                                                                                                                    //
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class Search2DMatrix {

    public static boolean solve(int[][] input, int target) {
        if(input == null || input.length == 0 || input[0].length == 0) return false;

        int length = input[0].length;

        // first select row
        int row = 0;
        int top = 0;
        int bottom = input.length - 1;

        while(top < bottom) {
            int middle = (top + bottom) / 2;
            if(input[middle][0] > target) {
                bottom = middle - 1;
            } else if(input[middle][length - 1] < target) {
                top = middle + 1;
            } else {
                row = middle;
                break;
            }
        }

        int left = 0;
        int right = input[0].length - 1;

        while(left < right) {
            int middle = (left + right) / 2;
            if(input[row][middle] > target) {
                right = middle - 1;
            } else if(input[row][middle] < target) {
                left = middle + 1;
            } else {
                return true;
            }
        }

        return false;
    }

    @Test
    public void test() {
        assertThat(solve(new int[][]{
        }, 3)).isFalse();

        assertThat(solve(new int[][]{
                {1, 3, 5, 7},
        }, 3)).isTrue();

        assertThat(solve(new int[][]{
                {1, 3, 5, 7},
        }, 13)).isFalse();

        assertThat(solve(new int[][]{
                {1, 3, 5, 7},
                {10, 11, 16, 20},
                {23, 30, 34, 60}
        }, 3)).isTrue();

        assertThat(solve(new int[][]{
                {1, 3, 5, 7},
                {10, 11, 16, 20},
                {23, 30, 34, 60}
        }, 13)).isFalse();
    }
}
