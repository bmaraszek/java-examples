package leetcode.examples.twoPointers;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                                                                                                                    //
//  A phrase is a palindrome if, after converting all uppercase letters into lowercase letters and removing all       //
//  non-alphanumeric characters, it reads the same forward and backward.                                              //
//  Alphanumeric characters are letters and numbers.                                                                  //
//                                                                                                                    //
//                                                                                                                    //
//  Time complexity is O(N) - We pass the array once                                                                  //
//  Space complexity is O(1) - We only use local variables                                                            //
//                                                                                                                    //
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class ValidPalindrome {

    public static boolean solve(String input) {
        int left = 0;
        int right = input.length() - 1;

        while(left < right) {
            if(!Character.isLetterOrDigit(input.charAt(left))) {
                left++;
                continue;
            }

            if(!Character.isLetterOrDigit(input.charAt(right))) {
                right--;
                continue;
            }

            if(Character.toLowerCase(input.charAt(left)) != Character.toLowerCase(input.charAt(right))) {
                return false;
            }

            left++;
            right--;
        }

        return true;
    }

    @Test
    public void test() {
        assertThat(solve("   ")).isEqualTo(true);
        assertThat(solve("race a car")).isEqualTo(false);
        assertThat(solve("A man, a plan, a canal: Panama")).isEqualTo(true);
    }
}
