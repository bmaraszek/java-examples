package leetcode.examples.string;

public class Palindrome {

    public boolean isPalindrome(String s) {
        int leftIdx = 0;
        int rightIdx = s.length() - 1;

        while(leftIdx < rightIdx) {
            if(s.charAt(leftIdx) != s.charAt(rightIdx)) return false;
            leftIdx++;
            rightIdx--;
        }
        return true;
    }
    public static void main(String... args) {
        Palindrome subject = new Palindrome();

        System.out.println("aba " + subject.isPalindrome("aba"));
        System.out.println("abba " + subject.isPalindrome("abba"));
        System.out.println("abbca " + subject.isPalindrome("abbca"));
    }
}
