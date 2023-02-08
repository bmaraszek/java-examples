package leetcode.examples.list;

import java.util.HashMap;
import java.util.Map;

class NodeRandom {
    int value;
    NodeRandom next;
    NodeRandom random;

    public NodeRandom(int value) {
        this.value = value;
    }
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                                                                                                                    //
//  We'll use a hashmap to keep the nodes in order                                                                    //
//  In the first pass, we'll allocate a map of (original_node : copied_node)                                          //
//  In the second pass, we'll go through the hashmap in order and link every node                                     //
//                                                                                                                    //
//  Time complexity is O(n) - We need to pass through the list twice                                                  //
//  Space complexity is O(n) - We make a copy containing N nodes                                                      //
//                                                                                                                    //
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class DeepCopyLinkedListWithRandomPointer {

    public static void printList(NodeRandom node) {
        while(node != null) {
            System.out.print("value: " + node.value);
            String next = node.next == null ? "null" : String.valueOf(node.next.value) + "(" + node.next + ")";
            System.out.print(" next: " + next);
            String random = node.random == null ? "null" : String.valueOf(node.random.value + "(" + node.next + ")");
            System.out.println(" random: " + random);
            node = node.next;
        }
    }

    public static NodeRandom deepCopy(NodeRandom node) {
        // if the argument is null, return immediately
        if(node == null) return node;

        // We'll store the references to random in a map
        Map<NodeRandom, NodeRandom> map = new HashMap<>();

        // Fill in the map first
        // We need to preserve the head of the original list, so let's copy it
        NodeRandom iter = node;
        while(iter != null) { // until we run into the last elem
            map.put(iter, new NodeRandom(iter.value)); // map the element to its copy
            iter = iter.next;
        }

        // Next, we assign the next and random pointers
        iter = node;
        while(iter != null) {
            map.get(iter).next = map.get(iter.next);
            map.get(iter).random = map.get(iter.random);
            iter = iter.next;
        }

        return map.get(node);
    }

    public static void main(String... args) {
        NodeRandom n1 = new NodeRandom(1);
        NodeRandom n2 = new NodeRandom(2);
        NodeRandom n3 = new NodeRandom(3);
        NodeRandom n4 = new NodeRandom(4);
        NodeRandom n5 = new NodeRandom(5);
        n1.next = n2;
        n2.next = n3;
        n3.next = n4;
        n4.next = n5;
        n1.random = n3;
        n2.random = n1;
        n3.random = n5;
        n4.random = n4;
        n5.random = n2;

        printList(n1);

        NodeRandom c1 = deepCopy(n1);
        System.out.println("--------");
        printList(c1);
    }
}

