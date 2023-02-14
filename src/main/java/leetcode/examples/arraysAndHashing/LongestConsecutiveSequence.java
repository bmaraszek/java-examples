package leetcode.examples.arraysAndHashing;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                                                                                                                    //
//  Given a list of integers nums, return the length of the longest consecutive elements sequence.                    //
//  Nums = [100, 4, 200, 1, 3, 2]                                                                                     //
//  Output: 4                                                                                                         //
//  Explanation: The longest consecutive sequence is [1, 2, 3, 4]                                                     //
//                                                                                                                    //
//  Simple solution is to sort the numbers and do one pass checking the sequences.                                    //
//                                                                                                                    //
//  A linear time solution is to put all the numbers in a HashSet.                                                    //
//  For each number N, check if (N-1) is in the set i.e. is this the start of a sequence                              //
//  If number N is the start of a sequence, we check (N+1), then (N+2), and so on.                                    //
//                                                                                                                    //
//  Time complexity is O(N) - We visit each number at most twice                                                      //
//  Space complexity is O(N) - When N is the size of the input array                                                  //
//                                                                                                                    //
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class LongestConsecutiveSequence {

    public static int longestSequence(List<Integer> nums) {
        Set<Integer> set = Set.copyOf(nums);
        int longestSequence = 0;
        int currentSequence = 1;

        for(Integer num : nums) {
            if(set.contains(num - 1)) continue;
            int currentNum = num;
            while(set.contains(++currentNum)) {
                longestSequence++;
            }
            if(currentSequence > longestSequence) longestSequence = currentSequence;
        }
        return  longestSequence;
    }

    @Test
    public void test() {
        assertThat(longestSequence(List.of(100, 4, 200, 1, 3, 2))).isEqualTo(4);
    }
}
