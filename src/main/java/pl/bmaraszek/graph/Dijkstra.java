package pl.bmaraszek.graph;

import org.junit.jupiter.api.Assertions;

import java.util.*;

public class Dijkstra {

    public static void main(String[] args){
        System.out.println("Dijkstra's Algorithm");

        int start = 1;
        int[][][] edges = {
                {{1, 7}},
                {{2, 6}, {3, 20}, {4, 3}},
                {{3, 14}},
                {{4, 2}},
                {},
                {}
        };

//        {"edges": [[[1, 2]], [[0, 1]], [[3, 1]], [[2, 2]]], "start": 1}

//        edges = new int[][][]{
//                {{1, 2}},
//                {{0, 1}},
//                {{3, 1}},
//                {{2, 2}}
//        };

        int[] expected = {0, 7, 13, 27, 10, -1};
        int[] actual = new Dijkstra().dijkstrasAlgorithm(start, edges);

        System.out.println(String.format("Expected: %s", Arrays.toString(expected)));
        System.out.println(String.format("Actual  : %s", Arrays.toString(actual)));

        Assertions.assertTrue(expected.length == actual.length);
        for (int i = 0; i < expected.length; i++) {
            Assertions.assertTrue(expected[i] == actual[i]);
        }
    }

    public void prepopulate(PriorityQueue<Item> nodesToVisit, int start, int size) {
        for(int i = 0; i < size; i++) {
            nodesToVisit.add(new Item(i, Integer.MAX_VALUE));
        }
        Item startNode = new Item(start, 0);
        nodesToVisit.remove(startNode);
        nodesToVisit.add(startNode);
    }

    public int[] dijkstrasAlgorithm(int start, int[][][] edges) {
        int numberOfVertices = edges.length;

        // init the result array
        int[] result = new int[numberOfVertices];
        Arrays.fill(result, Integer.MAX_VALUE);
        result[start] = 0;

        PriorityQueue<Item> nodesToVisit = new PriorityQueue<>();
        HashSet<Integer> visitedNodes = new HashSet<>();
        prepopulate(nodesToVisit, start, numberOfVertices);

        while(!nodesToVisit.isEmpty()) {
            Item closestNode = nodesToVisit.poll();
            visitedNodes.add(closestNode.vertex);
            int[][] adjecentNodes = edges[closestNode.vertex];
            for(int[] node : adjecentNodes) {
                if(visitedNodes.contains(node[0])) continue;
                if(closestNode.distance == Integer.MAX_VALUE) break;
                int tentativeDistance = closestNode.distance + node[1];
                if(tentativeDistance < result[node[0]]) {
                    result[node[0]] = tentativeDistance;
                    Item newShortestDistance = new Item(node[0], tentativeDistance);
                    nodesToVisit.remove(newShortestDistance);
                    nodesToVisit.add(newShortestDistance);
                }
            }
        }

        return changeInfinityToMinusOne(result);
    }

    private static int[] changeInfinityToMinusOne(int[] arr) {
        return Arrays.stream(arr).map(x -> {
            if(x == Integer.MAX_VALUE) return -1;
            return x;
        }).toArray();
    }

    static class Item implements Comparable<Item> {
        int vertex;
        int distance;
        public Item(int vertex, int distance) {
            this.vertex = vertex;
            this.distance = distance;
        }

        public int compareTo(Item other) {
            return Integer.compare(distance, other.distance);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Item item = (Item) o;
            return vertex == item.vertex;
        }

        @Override
        public int hashCode() {
            return Objects.hash(vertex);
        }
    }

}
