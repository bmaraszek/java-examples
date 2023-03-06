package neetcode.examples.twoPointers;

import org.junit.jupiter.api.Test;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static org.assertj.core.api.Assertions.assertThat;

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                                                                                                                    //
//  You are given an integer array 'height' of length n. There are n vertical lines drawn such that the two endpoints //
//  of the i-th line are (i, 0) and (i, height[i]).                                                                   //
//  Find two lines that together with the x-axis form a container, such that the container contains the most water.   //
//  Return the maximum amount of water a container can store. You may not slant the container.                        //
//                                                                                                                    //
//  The brute force solution is to calculate all the pairs.                                                           //
//                                                                                                                    //
//  Time complexity is O(n^2) - we have n^2 pair                                                                      //
//  Space complexity is O(1) - we only allocate variables to calculate the volume                                     //
//                                                                                                                    //
//  Better solution:                                                                                                  //
//  We'll initialize left pointer to the first index and right pointer to the last index. Why?                        //
//  We want to maximise the area, so let's consider the biggest distance first.                                       //
//  Calculate the volume, and then move the pointer pointing to the *smaller* value.                                  //
//  This way we can potentially increase the volume. Moving the bigger pointer will never increase the volume.        //
//                                                                                                                    //
//  Time complexity is O(n) - we pass the array once                                                                  //
//  Space complexity is O(1) - we only allocate variables to calculate the volume                                     //
//                                                                                                                    //
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class ContainerWithMostWater {

    public static int solve(int[] input) {
        if (input.length < 2) return 0;
        int maxVolume = 0;

        int left = 0;
        int right = input.length - 1;

        while(left < right) {
            int volume = min(input[left], input[right]) * (right - left);
            maxVolume = max(maxVolume, volume);
            if(input[left] < input[right]) {
                left++;
            } else {
                // if right value is smaller, we move the right pointer
                // if equal, we just move any pointer
                right--;
            }
        }
        return maxVolume;
    }

    @Test
    public void test() {
        assertThat(solve(new int[]{})).isEqualTo(0);
        assertThat(solve(new int[]{2})).isEqualTo(0);
        assertThat(solve(new int[]{0, 5, 0})).isEqualTo(0);
        assertThat(solve(new int[]{1, 1})).isEqualTo(1);
        assertThat(solve(new int[]{1, 8, 6, 2, 5, 4, 8, 3, 7})).isEqualTo(49);
    }
}
