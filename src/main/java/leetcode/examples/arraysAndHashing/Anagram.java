package leetcode.examples.arraysAndHashing;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                                                                                                                    //
//  We'll use a hashmap to count the occurances of each letter and then compare them.                                 //
//  Another approach is to sort the strings.                                                                          //
//                                                                                                                    //
//  Time complexity is O(A + B) - Where A and B are the lengths of the String                                         //
//  Space complexity is O(A) - We allocate a map with a maximum of A characters in it                                 //
//                                                                                                                    //
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class Anagram {

    public static boolean isAnagram(String a, String b) {
        if(a.length() != b.length()) return false;

        Map<Character, Integer> map = new HashMap<>();

        for(char c : a.toCharArray()) {
            map.put(c, 1 + map.getOrDefault(c, 0));
        }

        for(char c : b.toCharArray()) {
            Integer count = map.get(c);
            if(count == null) return false;
            map.put(c, count - 1);
        }

        return true;
    }

    public static boolean isAnagramSort(String a, String b) {
        char[] arrA = a.toCharArray();
        char[] arrB = b.toCharArray();
        Arrays.sort(arrA);
        Arrays.sort(arrB);
        return Arrays.compare(arrA, arrB) == 0;
    }

    @Test
    @DisplayName("Strings of different lengths are not anagrams")
    void shouldReturnFalseForStringsOfDifferentLengths() {
        assertThat(isAnagram("a", "abc")).isFalse();

        assertThat(isAnagramSort("a", "abc")).isFalse();
    }

    @Test
    @DisplayName("Should return true for valid anagrams")
    void shouldReturnTrue() {
        assertThat(isAnagram("abc", "cba")).isTrue();
        assertThat(isAnagram("kajak", "kkjaa")).isTrue();
        assertThat(isAnagram("Luke Skywalker", "rekeL uklawykS")).isTrue();

        assertThat(isAnagramSort("abc", "cba")).isTrue();
        assertThat(isAnagramSort("kajak", "kkjaa")).isTrue();
        assertThat(isAnagramSort("Luke Skywalker", "rekeL uklawykS")).isTrue();
    }

    @Test
    @DisplayName("Should return false for strings that are not anagrams")
    void shouldReturnFalse() {
        assertThat(isAnagram("abc", "bcd")).isFalse();
        assertThat(isAnagram("kajak", "kkajae")).isFalse();
        assertThat(isAnagram("Luke Skywalker", "rekel uklawyks")).isFalse();

        assertThat(isAnagramSort("abc", "bcd")).isFalse();
        assertThat(isAnagramSort("kajak", "kkajae")).isFalse();
        assertThat(isAnagramSort("Luke Skywalker", "rekel uklawyks")).isFalse();
    }
}
