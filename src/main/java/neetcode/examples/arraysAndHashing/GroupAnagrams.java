package neetcode.examples.arraysAndHashing;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                                                                                                                    //
//  Input: ["nat", "abc", "tan", "cba", "bca"]                                                                        //
//  Output: [["nat", "tan"], ["abc", "cba", "bca]]                                                                    //
//                                                                                                                    //
//  First approach is to sort every word and use the sorted word as a key in a HashMap<String, List<String>>.         //
//  Later, when we get the same key from another word with the same key, we append it to the appropriate list.        //
//                                                                                                                    //
//  Time complexity is O(M * N log N) - where M is the input length and N is the average length of the input string   //
//  Space complexity is O(M) - we allocate a hashmap with at most M elements                                          //
//                                                                                                                    //
//  Alternatively: Assuming we use 26 lowercase letters a-z                                                           //
//  Make a count of all the characters like this: "1a1n1t" and use this as a key                                      //
//                                                                                                                    //
//  Time complexity is O(M * N * 26) - where M is the input length and N is the average length of the input string    //
//  Space complexity is O(M) - we count characters in each word                                                       //
//                                                                                                                    //
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class GroupAnagrams {

    public static List<List<String>> anagramsSort(List<String> words) {
        Map<String, List<String>> map = new HashMap<>();

        for (String word : words) {
            char[] sortedWord = word.toCharArray();
            Arrays.sort(sortedWord);
            String key = new String(sortedWord);

//            if(map.containsKey(key)) {
//                map.get(key).add(word);
//            } else {
//                List<String> newList = new ArrayList<>();
//                newList.add(word);
//                map.put(key, newList);
//
//          this is equivalent to:
            map.computeIfAbsent(key, k -> new ArrayList<>());
            map.get(key).add(word);
        }

        List<List<String>> result = new ArrayList<>();
        result.addAll(map.values());
        return result;
    }

    public static List<List<String>> anagramsCount(List<String> words) {
        List<List<String>> result = new ArrayList<>();
        if (words.size() == 0) return result;
        Map<String, List<String>> map = new HashMap<>();

        for (String word : words) {
            int[] hash = new int[26];
            for (char c : word.toCharArray()) {
                hash[c - 'a']++;
            }
            String key = new String(Arrays.toString(hash));
            map.computeIfAbsent(key, k -> new ArrayList<>());
            map.get(key).add(word);
        }
        result.addAll(map.values());
        return result;
    }

    @Test
    public void test() {
        assertThat(anagramsSort(List.of("nat", "abc", "tan", "cba", "bca")))
                .containsExactlyInAnyOrder(List.of("nat", "tan"), List.of("abc", "cba", "bca"));

        assertThat(anagramsCount(List.of("nat", "abc", "tan", "cba", "bca")))
                .containsExactlyInAnyOrder(List.of("nat", "tan"), List.of("abc", "cba", "bca"));

        System.out.println("The integer value of lowercase 'a' is: " + (int) 'a');
        System.out.println("The integer value of uppercase 'A' is: " + (int) 'A');
    }
}
