package pl.bmaraszek;

import java.util.HashSet;
import java.util.Set;

public class BiValued {

    public static void main(String[] args) {
        int[] arr1 = {4, 2, 2, 4, 2};
        int[] arr2 = {1,2,3,2};
        int[] arr3 = {0,5,4,4,5,12};

        BiValued solver = new BiValued();
        System.out.println(solver.solve(arr1));
        System.out.println(solver.solve(arr2));
        System.out.println(solver.solve(arr3));
    }

    public int solve(int[] arr) {
        if(arr.length <= 2) return arr.length;
        int start = 0;
        int end = 2;
        int best = end - start - 1;
        while(end <= arr.length) {
            if(numberOfDistinctElems(arr, start, end) <= 2) {
                end++;
                best = Math.max(best, end - start - 1);
            } else {
                start++;
            }
        }
        return best;
    }

    public int numberOfDistinctElems(int[] arr, int start, int end) {
        Set<Integer> ints = new HashSet<>();
        for(int i = start; i < end; i++) {
            ints.add(arr[i]);
        }
        return ints.size();
    }
}
