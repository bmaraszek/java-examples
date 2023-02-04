package leetcode.examples.bloomberg;

import static java.lang.System.*;

public class AllSubsetsOfAString {

    /*
     * Given a string, print all subsets (not permutations)
     * "abc" -> empty, a, b, c, ab, ac, bc, abc
     *
     * This is also called a power set.
     * The number of solutions is 2^(size of the set)
     */

    public static void solution1(String s) {
        /*
         * https://www.geeksforgeeks.org/finding-all-subsets-of-a-given-set-in-java/?tab=article
         * Generate all binary numbers between 0 and 2^(set size)
         * Print all elements of the est corresponding to 1s
         */
        char[] string = s.toCharArray();
        int length = string.length;

        out.println("Size of the est is (1 << length) = " + (1 << length));

        for(int i = 0; i < (1 << length); i++ ){ // i between 0 and 7
            out.print("{ ");
            for(int j = 0; j < length; j++) {
                if((i & (1 << j)) > 0) {
                    out.print(string[j] + " ");
                }
            }
            out.println("}");
        }
    }

    public static void main(String... args) {
        solution1("abc");
    }
}
