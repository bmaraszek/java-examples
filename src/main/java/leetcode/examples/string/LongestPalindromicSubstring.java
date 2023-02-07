package leetcode.examples.string;

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                                                                                                                    //
//  The trick is to consider each letter as a possible middle of a palindrome (odd number of characters palindrome)   //
//  You must also consider all the spaces between the individual characters (even number of characters palindrome)    //
//                                                                                                                    //
//  Time complexity is O(n^2) - for each letter L we expand up to the length of the string                            //
//  Space complexity is O(1) - we can store just the start and end index                                              //
//                                                                                                                    //
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class LongestPalindromicSubstring {

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //                                                                                                                //
    //  We'll check for a palindrome at each character and each space between characters                         //
    //                                                                                                                //
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static String longestPalindromicSubstring(String s) {
        int[] currentLongest = new int[]{0 ,1};

        for(int i = 0; i < s.length(); i++) {
            int[] odd = getLongestPalindromeFrom(s, i - 1, i + 1);
            int[] even = getLongestPalindromeFrom(s, i - 1, i);
            int[] longest = odd[1] - odd[0] > even[1] - even[0] ? odd : even;
            currentLongest = currentLongest[1] - currentLongest[0] > longest[1] - longest[0] ? currentLongest : longest;
        }

        return s.substring(currentLongest[0], currentLongest[1]);
    }

    public static int[] getLongestPalindromeFrom(String s, int leftIdx, int rightIdx) {
        while(leftIdx >= 0 && rightIdx < s.length()) {
            if(s.charAt(leftIdx) != s.charAt(rightIdx)) {
                break;
            }
            leftIdx--;
            rightIdx++;
        }
        return new int[] {leftIdx + 1, rightIdx};
    }

    public static void main(String... args) {
        System.out.println("abcdefghij " + longestPalindromicSubstring("abcdefghij"));
        System.out.println("abaxyzzyxf " + longestPalindromicSubstring("abaxyzzyxf"));
    }
}
