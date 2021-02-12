package pl.bmaraszek.graph;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class AStar {

    static class Node implements Comparable<Node> {
        String id;
        int y;
        int x;
        int value;
        int distanceFromStart;      // G
        int estimatedDistanceToEnd; // H - manhattan distance
        Node cameFrom;				// F score = H + G

        public Node(int y, int x, int value) {
            this.id = String.format("%d-%d", y, x);
            this.y = y;
            this.x = x;
            this.value = value;
            this.distanceFromStart = Integer.MAX_VALUE;
            this.estimatedDistanceToEnd = Integer.MAX_VALUE;
            this.cameFrom = null;
        }

        @Override
        public String toString() {
            return String.format("%d-%d", y, x);
        }

        @Override
        public int compareTo(Node o) {
            return Integer.compare(this.estimatedDistanceToEnd, o.estimatedDistanceToEnd);
        }
    }

    private static int[][] reconstructPath(Node end) {
        if(end.cameFrom == null) return new int[][] {};

        Node currentNode = end;
        List<List<Integer>> path = new ArrayList<List<Integer>>();

        while(currentNode != null) {
            List<Integer> nodeData = new ArrayList<Integer>();
            nodeData.add(currentNode.y);
            nodeData.add(currentNode.x);
            path.add(nodeData);
            currentNode = currentNode.cameFrom;
        }

        int[][] result = new int[path.size()][2];
        for(int i = 0; i < result.length; i++) {
            result[i][0] = path.get(result.length - 1 - i).get(0);
            result[i][1] = path.get(result.length - 1 - i).get(1);
        }
        return result;
    }

    private static List<List<Node>> initializeNodes(int[][] graph) {
        List<List<Node>> result = new ArrayList<>();

        for(int i = 0; i < graph.length; i++) {
            List<Node> row = new ArrayList<Node>();
            for(int j = 0; j < graph[i].length; j++) {
                row.add(new Node(i, j, graph[i][j]));
            }
            result.add(row);
        }

        return result;
    }

    private static int calculateManhattanDistance(Node start, Node end) {
        return Math.abs(start.y - end.y) + Math.abs(start.x - end.x);
    }

    private static List<Node> getNeighbours(Node node, List<List<Node>> nodes) {
        List<Node> result = new ArrayList<Node>();
        int ySize = nodes.size();
        int xSize = nodes.get(0).size();

        if(node.y < ySize - 1)   result.add(nodes.get(node.y + 1).get(node.x)); // DOWN
        if(node.y > 0) 			 result.add(nodes.get(node.y - 1).get(node.x)); // UP
        if(node.x < xSize - 1)   result.add(nodes.get(node.y).get(node.x + 1)); // RIGHT
        if(node.x > 0) 			 result.add(nodes.get(node.y).get(node.x - 1)); // LEFT
        System.out.println("Visiting node " + node.toString() + ". Neighbours are: " + result);
        return result;
    }

    public static int[][] aStarAlgorithm(int startRow, int startCol, int endRow, int endCol, int[][] graph) {
        // use standard priority queue for min heap. Pass a comparator
        PriorityQueue<Node> nodesToVisit = new PriorityQueue<>(Comparator.comparingInt(n -> n.estimatedDistanceToEnd)
        );

        // init a Node graph corresponding to the array
        List<List<Node>> nodes = initializeNodes(graph);
        // in this example, we pass coordinates in the form of (y, x)
        Node startNode = nodes.get(startRow).get(startCol);
        Node endNode = nodes.get(endRow).get(endCol);

        // now we init the start node
        startNode.distanceFromStart = 0;
        startNode.estimatedDistanceToEnd = calculateManhattanDistance(startNode, endNode);

        // add the start node to the min heap
        nodesToVisit.add(startNode);

        while(!nodesToVisit.isEmpty()) {
            // traverse
            Node currentMinDistanceNode = nodesToVisit.poll();
            if(currentMinDistanceNode == endNode) break;

            List<Node> neighbours = getNeighbours(currentMinDistanceNode, nodes);
            for(Node neighbour : neighbours) {
                if(neighbour.value == 1) continue;  // SKIP OBSTACLES
                int tentativeDistanceToNeighbour = currentMinDistanceNode.distanceFromStart + 1;
                if(tentativeDistanceToNeighbour >= neighbour.distanceFromStart) continue; // SKIP VISITED
                System.out.println(String.format("Visiting node %s", neighbour.toString()));
                neighbour.cameFrom = currentMinDistanceNode;
                neighbour.distanceFromStart = tentativeDistanceToNeighbour;
                neighbour.estimatedDistanceToEnd = tentativeDistanceToNeighbour + calculateManhattanDistance(neighbour, endNode);

                if(!nodesToVisit.contains(neighbour)) {
                    nodesToVisit.add(neighbour);
                } else {
                    nodesToVisit.remove(neighbour);
                    nodesToVisit.add(neighbour);
                }
            }
        }

        return reconstructPath(endNode);
    }

    public static void main(String[] args) {
        int[][] graph = new int[][]{
                new int[]{0, 0, 0, 0, 0},
                new int[]{0, 1, 1, 1, 0},
                new int[]{0, 0, 0, 0, 0},
                new int[]{1, 0, 1, 1, 1},
                new int[]{0, 0, 0, 0, 0}
        };
        int[][] result = aStarAlgorithm(0,0, 4, 3, graph);

        for(int i = 0; i < result.length; i++) {
            for(int j = 0; j < result[0].length; j++)
                System.out.print(result[i][j] + ",");
            System.out.println();
        }
        System.out.println(result);

    }
}