package fragment.submissions;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class BartMaraszek {

    public static void main(String[] args) throws Exception {
        try (BufferedReader in = new BufferedReader(new FileReader(args[0]))) {
            in.lines()
                    .map(BartMaraszek::reassemble)
                    .forEach(System.out::println);
        }
    }

    public static String reassemble(String str) {
        List<String> tokens = new LinkedList<>(Arrays.asList(str.split(";")));
        while(tokens.size() > 1) {
            int longestOverlap = -1;
            int[] pairWithLongestOverlap = new int[]{-1, -1};
            for(int i = 0; i < tokens.size() - 1; i++) {
                for(int j = i + 1; j < tokens.size(); j ++) {
                    int currentOverlap = getLongestOverlapLengthBetween(tokens.get(i), tokens.get(j));
                    if(currentOverlap > longestOverlap) {
                        longestOverlap = currentOverlap;
                        pairWithLongestOverlap = new int[]{i, j};
                    }
                }
            }
            tokens.set(pairWithLongestOverlap[0], merge(tokens.get(pairWithLongestOverlap[0]), tokens.get(pairWithLongestOverlap[1]), longestOverlap));
            tokens.remove(pairWithLongestOverlap[1]);
        }
        return tokens.get(0);
    }

    public static int getLongestOverlapLengthBetween(String s1, String s2) {
        if(s1.contains(s2)) return s2.length();
        else if(s2.contains(s1)) return s1.length();
        int maxOverlap = 0;

        for(int i = 1; i < s2.length(); i++) {
            if(s1.startsWith(s2.substring(i))) {
                maxOverlap = s2.length() - i > maxOverlap ? s2.length() - i : maxOverlap;
            }
            if(s1.endsWith(s2.substring(0,i))) {
                maxOverlap = i > maxOverlap ? i : maxOverlap;
            }
        }
        return maxOverlap;
    }

    public static String merge(String s1, String s2, int overlap) {
        // if either string contains the other, return the longer string
        if(overlap >= s1.length()) return s2;
        if(overlap >= s2.length()) return s1;

        // we know the overlap size, so only 2 cases are possible
        if(s2.startsWith(s1.substring(s1.length() - overlap))) {
            return s1 + s2.substring(overlap);
        }

        // the only other way is if(s1.startsWith(s2.substring(s2.length() - overlap)))
        return s2 + s1.substring(overlap);
    }

}
