package leetcode.examples;

import org.junit.jupiter.api.Assertions;

import java.util.*;
import java.util.stream.Collectors;

public class Rivers {

    public static void main(String[] args) {
        int[][] input = {
                {1, 0, 0, 1, 0},
                {1, 0, 1, 0, 0},
                {0, 0, 1, 0, 1},
                {1, 0, 1, 0, 1},
                {1, 0, 1, 1, 0},
        };
        int[] expected = {1, 2, 2, 2, 5};
        List<Integer> output = Rivers.riverSizes(input);
        System.out.println("Output: " + output);
        Assertions.assertTrue(output.containsAll(Arrays.asList(expected)));
    }

    public static List<Integer> riverSizes(int[][] matrix) {
        // turn to nodes
        Queue<Node> nodesToVisit = new LinkedList<>();

        List<Integer> result = new ArrayList<>();
        HashSet<Node> visitedNodes = new HashSet<>();

        int xSize = matrix[0].length;
        int ySize = matrix.length;
        int currentRiverLength = 0;

        for(int y = 0; y < ySize; y++) {
            for(int x = 0; x < xSize; x++) {
                Node currentNode = new Node(y, x, matrix[y][x]);
                nodesToVisit.add(currentNode);
                while(!nodesToVisit.isEmpty()) {
                    Node node = nodesToVisit.poll();
                    if(visitedNodes.contains(node) || node.val == 0) continue;
                    visitedNodes.add(node);
                    // it's a 1
                    currentRiverLength++;
                    List<Node> neighbours = getUnvisitedNeighbours(node, visitedNodes, matrix);
                    nodesToVisit.addAll(neighbours);
                }
                if(currentRiverLength > 0) result.add(currentRiverLength);
                currentRiverLength = 0;
            }
        }

        return result;
    }

    public static List<Node> getUnvisitedNeighbours(Node node, Collection<Node> visitedNodes, int[][] arr) {
        List<Node> result = new ArrayList<>();
        // left
        if(node.x > 0) result.add(new Node(node.y, node.x-1, arr[node.y][node.x-1]));
        // up
        if(node.y > 0) result.add(new Node(node.y - 1, node.x, arr[node.y-1][node.x]));
        // right
        if(node.x < arr[0].length - 1) result.add(new Node(node.y, node.x+1, arr[node.y][node.x+1]));
        // down
        if(node.y < arr.length - 1) result.add(new Node(node.y+1, node.x, arr[node.y+1][node.x]));
        // filter out visited
        return result.stream().filter(x -> !visitedNodes.contains(x)).collect(Collectors.toList());
    }

    public static class Node {
        int x;
        int y;
        int val;
        public Node(int y, int x, int val) {
            this.x = x;
            this.y = y;
            this.val = val;
        }
        @Override
        public boolean equals(Object o) {
            if(this == o) return true;
            return o instanceof Node && this.x == ((Node)o).x && this.y == ((Node)o).y;
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(x) * Objects.hashCode(y);
        }

        @Override
        public String toString() {
            return String.format("Node(y: %d, x: %d, val: %d)", y, x, val);
        }
    }
}
