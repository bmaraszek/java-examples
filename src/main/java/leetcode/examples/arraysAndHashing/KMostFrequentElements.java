package leetcode.examples.arraysAndHashing;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                                                                                                                    //
//  Given an integer list elems and an integer k, return the k most frequent elements (in any order)                  //
//                                                                                                                    //
//  There are 3 distinct ways to solve this.                                                                          //
//                                                                                                                    //
//  1. Count the elems in a HashMap. Then get the top k elements (can use Stream API).                                //
//     In this case we need to count all elements and then sort all elements.                                         //
//                                                                                                                    //
//  Time complexity is O(N log N) - We need to sort results of N elements                                             //
//  Space complexity is O(N) - We allocate a map with N elements. Then we save sorted N.                              //
//                                                                                                                    //
//  2. Count the elems in a HashMap. Then get the elems through a Max Heap, so that we don't have to sort all.        //
//                                                                                                                    //
//  Time complexity is O(n log k) - where K is the limit, N is the number of elements                                 //
//  Space complexity is O(N) - we allocate a map with N elements                                                      //
//                                                                                                                    //
//  3. Modified Bucket Sort.                                                                                          //
//     Allocate an array of length N. Use the array index as the frequency of each element.                           //
//     The loop through the array backwards and return the first k entries.                                           //
//                                                                                                                    //
//  Time complexity is O(N) - we count the frequencies O(N) and later make another pass to populate an array O(N)     //
//                            then, we loop through k < N entries in the array.                                       //
//                                                                                                                    //
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class KMostFrequentElements {
    public static List<Integer> kMostFrequentMap(List<Integer> elems, int k) {
        Map<Integer, Integer> map = new HashMap<>();

        for(Integer elem : elems) {
            map.put(elem, map.getOrDefault(elem, 0) + 1);
        }

        List sortedResults = map.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .map(e -> e.getKey())
                .collect(Collectors.toList());

        return sortedResults.subList(0, k);
    }

    public static List<Integer> kMostFrequentHeap(List<Integer> elems, int k) {
        Map<Integer, Integer> map = new HashMap<>();

        for(Integer elem : elems) {
            map.put(elem, map.getOrDefault(elem, 0) + 1);
        }

        // In Java, the size of a Heap (PriorityQueue) is not bounded.
        // If we want a Max Heap of a bounded size, we must instead use a Min Heap and manually evict
        PriorityQueue<Map.Entry<Integer, Integer>> heap = new PriorityQueue<>(Comparator.comparingInt(Map.Entry::getValue));

        for(Map.Entry entry : map.entrySet()) {
            heap.add(entry);
            if(heap.size() > k) heap.poll();
        }

        List<Integer> result = new ArrayList<>();
        for(int i = 0; i < k; i++) result.add(heap.poll().getKey());
        return result;
    }

    public static List<Integer> kMostFrequentBucketSort(List<Integer> elems, int k) {
        Map<Integer, Integer> map = new HashMap<>();
        // The bucket NEEDS to be an array to pre-allocate the full size
        List<Integer>[] bucket = new List[elems.size() + 1];
        List<Integer> result = new ArrayList<>();

        for(Integer elem : elems) {
            map.put(elem, map.getOrDefault(elem, 0) + 1);
        }

        for(Map.Entry<Integer, Integer> entry : map.entrySet()) {
            if(bucket[entry.getValue()] == null) bucket[entry.getValue()] = new ArrayList<>();
            bucket[entry.getValue()].add(entry.getKey());
        }

        for(int i = bucket.length - 1; i > 0; i--) {
            if(bucket[i] != null) result.addAll(bucket[i]);
            if(result.size() >= k) break;
        }

        return result.subList(0, k);
    }

    @Test
    public void test() {
        assertThat(kMostFrequentMap(List.of(1, 1, 1, 2, 2, 3), 2)).containsExactlyInAnyOrder(1, 2);
        assertThat(kMostFrequentMap(List.of(1, 1, 1, 2, 2, 3, 4, 4, 4, 4, 5), 2)).containsExactlyInAnyOrder(1, 4);

        assertThat(kMostFrequentHeap(List.of(1, 1, 1, 2, 2, 3), 2)).containsExactlyInAnyOrder(1, 2);
        assertThat(kMostFrequentHeap(List.of(1, 1, 1, 2, 2, 3, 4, 4, 4, 4, 5), 2)).containsExactlyInAnyOrder(1, 4);

        assertThat(kMostFrequentBucketSort(List.of(1, 1, 1, 2, 2, 3), 2)).containsExactlyInAnyOrder(1, 2);
        assertThat(kMostFrequentBucketSort(List.of(1, 1, 1, 2, 2, 3, 4, 4, 4, 4, 5), 2)).containsExactlyInAnyOrder(1, 4);
    }
}
