package neetcode.examples.twoPointers;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                                                                                                                    //
//  Given an integer array nums, return all triplets {num[i], num[j], num[k]} such that i != j != k and               //
//  nums[i] + nums[j] + nums[k] = 0. The solution must not contain duplicate triplets.                                //
//                                                                                                                    //
//  The solution is to first sort the array, and then loop through the array choosing the first number A,             //
//  performing a 2Sum on the reminder of the array                                                                    //
//                                                                                                                    //
//  Time complexity is O(n^2) - We move do a 2sum n times                                                             //
//  Space complexity is O(1) - We only alocate the pointers                                                           //
//                                                                                                                    //
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class ThreeSum {

    public static List<List<Integer>> solve(int[] input) {
        if(input == null || input.length < 3) return List.of(List.of(), List.of());

        List<List<Integer>> result = new ArrayList<>();

        for(int i = 0; i < input.length - 2; i++) {
            if(i > 0 && input[i] == input[i - 1]) {
                // if the next number is the same, we pass it over to avoid duplicates.
                continue;
            }

            int left = i + 1;
            int right = input.length - 1;

            while(left < right) {
                int sum = input[i] + input[left] + input[right];
                if(sum == 0) {
                    List<Integer> solution = List.of(input[i], input[left], input[right]);
                    result.add(solution);
                    // we skip duplicates
                    while(left < right && input[left] == input[left + 1]) left++;
                    while(left < right && input[right] == input[right - 1]) right--;
                    left++;
                    right--;
                } else if(sum < 0) {
                    left++;
                } else {
                    right--;
                }
            }
        }
        return result;
    }

    @Test
    public void test() {
        assertThat(solve(new int[]{-1, 0, 1, 2, -1, 4})).containsExactlyInAnyOrder(List.of(-1, 2, -1), List.of(0, 1, -1));
        assertThat(solve(new int[]{-3, 3, 4, -3, 1, 2})).isEqualTo(List.of(List.of(-3, 1, 2)));
    }
}
