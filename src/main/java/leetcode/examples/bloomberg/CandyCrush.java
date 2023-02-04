package leetcode.examples.bloomberg;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CandyCrush {

    public static String crush(String s) {
        char[] chars = s.toCharArray();
        boolean crushed = true;

        while(crushed) {
            int start = 0;
            int end = 0;
            int consecutiveCharacterCount = 0;
            crushed = false;
            while(start < chars.length) {
                while(end < chars.length && (chars[end] == chars[start] || chars[end] == '0')) {
                    if(chars[end] != '0') {
                        consecutiveCharacterCount++;
                    }
                    end++;
                }
                if(consecutiveCharacterCount >= 3) {
                    markAsRemoved(chars, start, end);
                    crushed = true;
                }
                consecutiveCharacterCount = 0;
                start = end;
            }
        }

        return charsToString(chars);
    }

    private static void markAsRemoved(char[] chars, int start, int end) {
        for(int i = start; i < end; i++) {
            chars[i] = '0';
        }
    }

    private static String charsToString(char[] chars) {
        StringBuilder sb = new StringBuilder();
        for(char c : chars) {
            if(c != '0') sb.append(c);
        }
        String result = sb.toString();
        System.out.println("Returning: " + result);
        return result;
    }


    public static void main(String... args) {
        assertEquals("", crush("aaabbb"));
        assertEquals("c", crush("aaabbbc"));
        assertEquals("cd", crush("aabbbacd"));
        assertEquals("", crush("aabbccddeeedcba"));
        assertEquals("acd", crush("aaabbbacd"));
        assertEquals("", crush("aabbbaabbbaacccaa"));
    }
}
