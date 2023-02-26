package leetcode.examples.arraysAndHashing;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                                                                                                                    //
//  We'll put all numbers in a HashSet.                                                                                //
//  Next, for every number, we'll search the HashSet for (target - N)                                                 //
//                                                                                                                    //
//  Time complexity is O(N) - we pass the array once                                                                  //
//  Space complexity is O(N) - we allocate a hashmap with at most N elements                                          //
//                                                                                                                    //
//  Alternatively: Sort the array first. Use two pointers from each end.                                              //
//  Loop while L < R.                                                                                                 //
//  If the sum is smaller than sum, move the left pointer to the left.                                                //
//  If the sum is bigger than the sum, move the right pointer to the right.                                           //
//                                                                                                                    //
//  Time complexity is O(N nog N) - because we sort                                                                   //
//  Space complexity is O(1) - wa allocate 2 pointers                                                                 //
//                                                                                                                    //
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class TwoSum {

    public static int[] twoSumHashSet(int[] arr, int target) {
        Map<Integer, Integer> mapNumberToIndex = new HashMap<>();

        for(int i = 0; i < arr.length; i++) {
            int possiblePair = target - arr[i];
            if(mapNumberToIndex.containsKey(possiblePair)) return new int[] {mapNumberToIndex.get(possiblePair), i};
            mapNumberToIndex.put(arr[i], i);
        }

        return new int[]{-1, -1};
    }

    public static boolean twoSumPointers(int[] arr, int target) {
        Arrays.sort(arr);
        int l = 0;
        int p = arr.length - 1;

        while(l < p) {
            int currentSum = arr[l] + arr[p];
            if(currentSum == target) return true;
            if(currentSum < target) l++;
            if(currentSum > target) p++;
        }

        return false;
    }

    @Test
    public void test() {
        assertThat(twoSumHashSet(new int[]{3, 5, -4, 8, 11, 1, -1, 6}, 99)).containsExactly(-1, -1);
        assertThat(twoSumHashSet(new int[]{3, 5, -4, 8, 11, 1, -1, 6}, 10)).containsExactly(4, 6);

        assertThat(twoSumPointers(new int[]{3, 5, -4, 8, 11, 1, -1, 6}, 99)).isFalse();
        assertThat(twoSumPointers(new int[]{3, 5, -4, 8, 11, 1, -1, 6}, 10)).isTrue();
    }
}
