package leetcode.examples.bloomberg;

import java.util.ArrayList;
import java.util.List;

import static java.util.List.of;
import static org.assertj.core.api.Assertions.assertThat;

public class Powerset {

    public static List<List<Integer>> powersetIterative(List<Integer> array) {
        /*
         * Add an empty set to the result first - an empty set is a subset of every solution.
         * Iterate through all the numbers in the input list
         * For every number, iterate through every set already in the result
         * For every set S in the result, add a new set, which is the sum of S and the current number
         *
         * Time complexity: O(n * 2^n)
         * Space complexity: O(n * 2^n) - this many solutions
         */
        List<List<Integer>> subsets = new ArrayList<>();
        subsets.add(List.of());

        for(Integer elem : array) {
            int length = subsets.size();
            for(int i = 0; i < length; i++) {
                List<Integer> currentSubset = new ArrayList<>(subsets.get(i));
                currentSubset.add(elem);
                subsets.add(currentSubset);
            }
        }
        return subsets;
    }

    public static List<List<Integer>> powersetRecursive(List<Integer> arr) {
        return new ArrayList<>();
    }

    public static void main(String... args) {

        List<List<Integer>> expected = of(of(), of(1), of(2), of(3), of(1,2), of(1,3), of(2,3), of(1,2,3));
        List<List<Integer>> actual = powersetIterative(of(1,2,3));

        assertThat(expected)
                .hasSize(actual.size())
                .hasSameElementsAs(actual);

        actual = powersetRecursive(of(1,2,3));

        assertThat(expected)
                .hasSize(actual.size())
                .hasSameElementsAs(actual);
    }
}
