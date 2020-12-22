package pl.bmaraszek;

public class PalindromeLessOne {
    public static void main(String[] args) {
        //System.out.println(isPalindrome("kajak", 0, 4));
        System.out.println(isPalindromeLessOne("kajak"));
        System.out.println(isPalindromeLessOne2("kajak"));

        System.out.println(isPalindromeLessOne("kajaks"));
        System.out.println(isPalindromeLessOne2("kajaks"));

        System.out.println(isPalindromeLessOne("ksajak"));
        System.out.println(isPalindromeLessOne2("ksajak"));

        System.out.println(isPalindromeLessOne("ksajaka"));
        System.out.println(isPalindromeLessOne2("ksajaka"));
    }

    public static boolean isPalindromeLessOne(String str) {
        for(int i = 0; i < str.length() / 2; i++) { // we can use str.length() / 2 because it's symmetrical
            if(str.charAt(i) != str.charAt(str.length() - i - 1)) { // if it's not a palindrome, just skip one
                return isPalindrome(str, i+1, str.length() - i - 1) ||
                        isPalindrome(str, i, str.length() - i - 2);
            }
        }
        return true;
    }

    public static boolean isPalindromeLessOne2(String str) {
        int left = 0;
        int right = str.length() - 1;
        while(left < right) {
            if(str.charAt(left) != str.charAt(right)) {
                return isPalindrome(str, left + 1, right) ||
                        isPalindrome(str, left, right - 1);
            }
            left++;
            right--;
        }
        return true;
    }

    /**
     * Normal palindrome check only with bounds as args
     * @param str
     * @param left
     * @param right
     * @return
     */
    public static boolean isPalindrome(String str, int left, int right) {
        while(left < right) {
            if(str.charAt(left) != str.charAt(right)) return false;
            left++;
            right--;
        }
        return true;
    }
}
