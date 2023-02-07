package leetcode.examples.string;

import java.util.HashMap;

public class FirstNonRepeatingCharacter {

    public static int fistNonRepeatingCharacter(String s) {
        HashMap<Character, Integer> charFrequencies = new HashMap<>();

        for(int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            charFrequencies.put(c, charFrequencies.getOrDefault(c, 0) + 1);
        }

        for(int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if(charFrequencies.get(c) == 1) {
                return i;
            }
        }
        return -1;
    }

    public static void main(String... args) {
        System.out.println("abcddca " + fistNonRepeatingCharacter("abcddca"));
        System.out.println("abccba " + fistNonRepeatingCharacter("abcba"));
    }
}
