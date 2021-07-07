package leetcode.examples.dynamic;

import java.util.Arrays;

public class NumberOfCoins {
    public static void main(String[] args) {
        int n = 3;
        int[] denoms = new int[]{2, 1};
        System.out.println(minNumberOfCoinsForChange(n, denoms));
    }

    public static int increment(int n) {
        if(n == Integer.MAX_VALUE) return Integer.MAX_VALUE;
        return n + 1;
    }

    public static int minNumberOfCoinsForChange(int n, int[] denoms) {
        int[] numOfCoins = new int[n+1]; // inc. zero
        Arrays.fill(numOfCoins, Integer.MAX_VALUE);
        numOfCoins[0] = 0;

        for(int denom : denoms) {
            for(int amount = 0; amount < numOfCoins.length; amount++) {
                if( denom <= amount) {
                    numOfCoins[amount] = Math.min(numOfCoins[amount], increment(numOfCoins[amount - denom]));
                }
            }
        }
        return numOfCoins[n] == Integer.MAX_VALUE ? -1 : numOfCoins[n];
    }
}
