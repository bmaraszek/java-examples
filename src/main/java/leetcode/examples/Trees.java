package leetcode.examples;

import java.util.Arrays;

public class Trees {

    public static void main(String[] args) {
        //System.out.println(solution(new int[]{1, 8, 7, 3, 4, 1, 8}, new int[]{6, 4, 1, 8, 5, 1, 7}));
        System.out.println(1 & 1);
        System.out.println(2 & 1);
        System.out.println(3 & 1);
        System.out.println(4 & 1);
        System.out.println(5 & 1);
    }

    public static int solution(int[] X, int[] Y) {
        // write your code in Java SE 11
        Arrays.sort(X);
        int maxDiff = 1;
        for(int i = 0; i < X.length - 1; i++) {
            int treeA = X[i];
            int treeB = X[i+1];
            int diff = treeB - treeA;
            maxDiff = diff > maxDiff ? diff : maxDiff;
        }
        return maxDiff;
    }
}
