package leetcode.examples.list;

class Node {
    int value;
    Node next;

    public Node(int value) {
        this.value = value;
    }
}

public class DeepCopyLinkedList {

    public static void printList(Node node) {
        while(node != null) {
            System.out.println("value: " + node.value + " node: " + node + " next: " + node.next);
            node = node.next;
        }
    }

    public static Node deepCopy(Node node) {
        if(node == null) return node;

        Node newHead = new Node(node.value);
        Node iter = newHead;

        while(node.next != null) {
            iter.next = new Node(node.next.value);
            node = node.next;
            iter = iter.next;
        }

        return newHead;
    }

    public static void main(String... args) {
        Node n1 = new Node(1);
        Node n2 = new Node(2);
        Node n3 = new Node(3);
        Node n4 = new Node(4);
        Node n5 = new Node(5);
        n1.next = n2;
        n2.next = n3;
        n3.next = n4;
        n4.next = n5;

        printList(n1);

        Node c1 = deepCopy(n1);
        System.out.println("--------");
        printList(c1);
    }
}

