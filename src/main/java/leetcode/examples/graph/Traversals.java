package leetcode.examples.graph;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.*;

public class Traversals {

    @AllArgsConstructor
    @EqualsAndHashCode
    @Getter
    static class Node {
        private String name;
        public String toString() {
            return name;
        }
    }

    @AllArgsConstructor
    @EqualsAndHashCode
    @Getter
    static class Graph {
        Map<Node, List<Node>> adjacencyMap;
    }

    private static Graph aGraph() {
        Node a = new Node("A");
        Node b = new Node("B");
        Node c = new Node("C");
        Node d = new Node("D");
        Node e = new Node("E");

        Map<Node, List<Node>> adjacencyMap = new HashMap<>();
        adjacencyMap.put(a, List.of(b, c));
        adjacencyMap.put(b, List.of(d, e));
        adjacencyMap.put(c, List.of(d));
        adjacencyMap.put(d, List.of(e));
        adjacencyMap.put(e, List.of());

        // A -> B -> E <-|
        // |      -> D --|
        // L-> C ---^

        // valid topological sortings is:
        // A, B, C, D, E
        // A, C, B, D, E
        return new Graph(adjacencyMap);
    }

    public static void bfsNonRecursive(Graph g, Node n) {
        // O(N + M) - number of nodes + number of edges
        Set<Node> isVisited = new HashSet<>();

        Queue<Node> queue = new ArrayDeque<>();
        queue.add(n);

        while(!queue.isEmpty()) {
            Node currentNode = queue.remove();
            if(!isVisited.contains(currentNode)) {
                isVisited.add(currentNode);
                visit(currentNode);

                for(Node neighbour : g.getAdjacencyMap().get(currentNode)) {
                    queue.add(neighbour);
                }
            }
        }
    }

    public static void bfsRecursive(Graph g, Node n) {
        Set<Node> isVisited = new HashSet<>();
        Queue<Node> queue = new ArrayDeque<>();
        queue.add(n);
        bfsRecursiveHelper(g, queue, isVisited);
    }

    private static void bfsRecursiveHelper(Graph g, Queue<Node> queue, Set<Node> isVisited) {
        if(queue.isEmpty()) return;
        Node currentNode = queue.remove();
        visit(currentNode);
        for(Node neighbour : g.getAdjacencyMap().get(currentNode)) {
            if(!isVisited.contains(neighbour)) {
                isVisited.add(neighbour);
                queue.add(neighbour);
            }
        }
        bfsRecursiveHelper(g, queue, isVisited);
    }

    public static void dfsNonRecursive(Graph g, Node n) {
        Stack<Node> stack = new Stack<>();
        Set<Node> isVisited = new HashSet<>();
        stack.push(n);

        while(!stack.isEmpty()) {
            Node current = stack.pop();
            if(!isVisited.contains(current)) {
                isVisited.add(current);
                visit(current);
                for(Node destination : g.getAdjacencyMap().get(current)) {
                    stack.push(destination);
                }
            }
        }
    }

    public static void dfsRecursive(Graph g, Node n) {
        Set<Node> isVisited = new HashSet<>();
        dfsRecursiveHelper(g, n, isVisited);
    }

    private static void dfsRecursiveHelper(Graph g, Node n, Set<Node> isVisited) {
        isVisited.add(n);
        visit(n); // any business logic
        List<Node> neighbours = g.getAdjacencyMap().get(n);
        for(Node neighbour : neighbours) {
            if(!isVisited.contains(neighbour)) dfsRecursiveHelper(g, neighbour, isVisited);
        }
    }

    public static List<Node> topologicalSort(Graph g, Node n) {
        LinkedList<Node> result = new LinkedList<>();
        Map<Node, Boolean> isVisited = new HashMap<>();
        g.adjacencyMap.forEach((k, v) -> isVisited.put(k, false));
        topologicalSortWithRecursion(n, isVisited, result);
        return result;
    }

    private static void topologicalSortWithRecursion(Node n, Map<Node, Boolean> isVisited, LinkedList<Node> result) {
        isVisited.put(n, true);
        for(Node destination : aGraph().getAdjacencyMap().get(n)) {
            if(!isVisited.get(destination)) {
                topologicalSortWithRecursion(destination, isVisited, result);
            }
        }
        result.addFirst(n);
    }

    private static void visit(Node n) {
        System.out.print(n.getName() + " ");
    }

    public static void main(String... args) {
        Graph g = aGraph();

        System.out.println("BFS non-recoursive (queue): ");
        bfsNonRecursive(g, new Node("A"));

        System.out.println("\n\nBFS recursive (queue): ");
        bfsRecursive(g, new Node("A"));

        System.out.println("\n\nDFS non-recursive (stack)");
        dfsNonRecursive(g, new Node("A"));

        System.out.println("\n\nDFS recursive:");
        dfsRecursive(g, new Node("A"));

        System.out.println("\n\nTopological sort:");
        List<Node> result = topologicalSort(g, new Node("A"));
        System.out.println(result);
    }
}
