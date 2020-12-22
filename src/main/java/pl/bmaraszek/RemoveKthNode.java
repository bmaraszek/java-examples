package pl.bmaraszek;

public class RemoveKthNode {

    public static void main(String[] args) {
        LinkedList head = new LinkedList(0);
        LinkedList list = head;
        for(int i = 1; i < 10; i++) {
            list.next = new LinkedList(i);
            list = list.next;
        }
        System.out.println(list.value);
        removeKthNodeFromEnd(head, 10);
        System.out.println("---");
        while(head != null) {
            System.out.print(head.value);
            head = head.next;
        }
    }

    public static void removeKthNodeFromEnd(LinkedList head, int k) {
        int counter = 0;
        LinkedList frontNode = head;
        LinkedList kNode = head;
        while(counter < k && frontNode != null) {
            counter++;
            frontNode = frontNode.next;
        }
        while(frontNode != null && frontNode.next != null) {
            frontNode = frontNode.next;
            kNode = kNode.next;
        }
        LinkedList toRemove = kNode.next;
        kNode.next = kNode.next.next;
        toRemove.next = null;
    }

    static class LinkedList {
        int value;
        LinkedList next = null;

        public LinkedList(int value) {
            this.value = value;
        }
    }
}
