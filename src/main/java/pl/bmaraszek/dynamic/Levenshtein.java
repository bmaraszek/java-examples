package pl.bmaraszek.dynamic;

import java.util.Arrays;

public class Levenshtein {

    public static void main(String[] args) {
        levenshteinDistance("bartek", "kebab");
    }

    public static int levenshteinDistance(String s1, String s2) {
        int[][] edits = new int[s2.length() + 1][s1.length() + 1];
        for(int i = 0; i < s2.length() + 1; i++) {
            for(int j = 0; j < s1.length() +1; j++) {
                edits[i][j] = j;
            }
            edits[i][0] = i;
        }
        System.out.println(Arrays.deepToString(edits));
        return 0;
    }
}
