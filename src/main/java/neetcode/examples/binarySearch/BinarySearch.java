package neetcode.examples.binarySearch;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                                                                                                                    //
//  Given an array of integers nums, which is sorted in ascending order, and an integer target, write a function      //
//  to search target in nums. If target exists, then return its index. Otherwise, return -1.                          //
//                                                                                                                    //
//  Time complexity is O(log n) - We halve the array every time                                                       //
//  Space complexity is O(1) - We do not allocate extra space                                                         //
//                                                                                                                    //
//  We're going to initialise 2 pointers: left to 0, and right to the last index of the array.                        //
//  Then continue to search while left <= right.                                                                      //
//  In the search method, we're going to calculate the middle index, and then compare the nums[middle] to the target. //
//  If input[middle] is more than the target, then we must look in the lower half i.e. move the right pointer.        //
//  If input[middle] is less than the target, then we must look in the higher half i.e. move the left pointer.        //
//  If input[middle] is equal to the target, we return middle.                                                        //
//  If the loop exits, we return -1 (target not found)                                                                //
//                                                                                                                    //
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class BinarySearch {

    public static int solve(int[] input, int target) {
        int left = 0;
        int right = input.length - 1;

        while(left <= right) {
            // danger here: this can overflow. Instead, you can use: left + ((right - left) / 2)
            // i.e. take the left pointer and add half the distance between right and left
            int middle = (left + right) / 2;
            if(input[middle] > target) {
                right = middle - 1;
            } else if(input[middle] < target) {
                left = middle + 1;
            } else {
                return middle;
            }
        }
        return -1;
    }

    public static int solveRecursive(int[] input, int target) {
        return binarySearchRecursive(input, 0, input.length - 1, target);
    }

    public static int binarySearchRecursive(int[] input, int left, int right, int target) {
        if(left > right) return -1;

        int middle = (left + right) / 2;
        if(input[middle] > target) {
            return binarySearchRecursive(input, left, middle - 1, target);
        } else if (input[middle] < target) {
            return binarySearchRecursive(input, middle + 1, right, target);
        } else {
            return middle;
        }
    }

    @Test
    public void test() {
        assertThat(solve(new int[]{}, 2)).isEqualTo(-1);

        assertThat(solve(new int[]{1}, 2)).isEqualTo(-1);
        assertThat(solve(new int[]{2}, 2)).isEqualTo(0);

        assertThat(solve(new int[]{-1, 0, 3, 5, 9, 12}, 2)).isEqualTo(-1);
        assertThat(solve(new int[]{-1, 0, 3, 5, 9, 12}, 9)).isEqualTo(4);

        assertThat(solve(new int[]{-1, 0, 3, 5, 9, 12, 15}, 2)).isEqualTo(-1);
        assertThat(solve(new int[]{-1, 0, 3, 5, 9, 12, 15}, 9)).isEqualTo(4);
    }

    @Test
    public void testRecursive() {
        assertThat(solveRecursive(new int[]{}, 2)).isEqualTo(-1);

        assertThat(solveRecursive(new int[]{1}, 2)).isEqualTo(-1);
        assertThat(solveRecursive(new int[]{2}, 2)).isEqualTo(0);

        assertThat(solveRecursive(new int[]{-1, 0, 3, 5, 9, 12}, 2)).isEqualTo(-1);
        assertThat(solveRecursive(new int[]{-1, 0, 3, 5, 9, 12}, 9)).isEqualTo(4);

        assertThat(solveRecursive(new int[]{-1, 0, 3, 5, 9, 12, 15}, 2)).isEqualTo(-1);
        assertThat(solveRecursive(new int[]{-1, 0, 3, 5, 9, 12, 15}, 9)).isEqualTo(4);
    }
}
