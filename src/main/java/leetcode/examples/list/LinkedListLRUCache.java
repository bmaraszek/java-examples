package leetcode.examples.list;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static java.lang.String.format;

/**
 * LRU cache is a combination of a HashMap and LinkedList.
 * We should not use Java LinkedList because removing a node from LinkedList is O(n).
 * Instead, if a node knows how to remove itself: node.remove(), it's O(1).
 *
 * Start programming outside-in, start with the methods: LRUCache.put(key, val) and LRUCache.get(key)
 * This will necessitate adding LRUCache.evictLastRecentlyUsed().
 *
 * Finish by implementing List and Node. Node should know how to remove itself from the list.
 * List needs: keeping track of the head and tail, List.addFirst(val) and List.removeLast() - this is the eviction
 *
 * Every time you touch an element, remove it from the list and addFirst().
 */
@Slf4j
public class LinkedListLRUCache<K, V> implements LRUCache<K, V> {

    @Data
    private static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> next;
        private Node<K, V> prev;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public void removeFromList() {
            if(prev != null) prev.next = next;
            if(next != null) next.prev = prev;
            prev = null;
            next = null;
        }

        public String toString() {
            return format("Node key: %s value: %s", key, value);
        }
    }

    @Data
    private static class List {
        private Node head;
        private Node tail;
        private int size = 0;

        public int size() {
            return size;
        }

        public boolean isEmpty() {
            return size == 0;
        }

        public List addFirst(Node node) {
            if(head == null && tail == null) {
                head = node;
                tail = node;
            } else {
                Node previousHead = getHead();
                previousHead.prev = node;
                node.setNext(previousHead);
                head = node;
            }
            size++;
            return this;
        }

        public Optional<Node> removeLast() {
            if(tail == null) {
                return Optional.empty();
            } else if(head == tail) {
                Node result = tail;
                head = null;
                tail = null;
                size--;
                return Optional.of(result);
            } else {
                Node result = tail;
                Node secondLast = tail.getPrev();
                tail.prev = null;
                tail = secondLast;
                size--;
                return Optional.of(result);
            }
        }

        public void clear() {
            while(tail != null) {
                removeLast();
            }
            size = 0;
        }

        public String toString() {
            if(head == null) return "()";
            StringBuilder sb = new StringBuilder();
            Node node = head;
            do {
                sb.append(format("(%s, %s) -> ", node.getKey(), node.getValue()));
                node = node.next;
            } while (node != null);
            return sb.toString();
        }
    }

    private int maxCapacity;
    private Map<K, Node<K, V>> map;
    List list;

    public LinkedListLRUCache(int maxCapacity) {
        this.maxCapacity = maxCapacity;
        this.map = new HashMap<>(maxCapacity);
        this.list = new List();
    }
    @Override
    public LRUCache<K, V> put(K key, V value) {
        if(list.size() + 1 > maxCapacity) {
            evictLeastRecentyUsed();
        }
        Node<K, V> newNode = new Node(key, value);
        map.put(key, newNode);
        list.addFirst(newNode);
        log.info("Added key: {} value: {}. List: {}", key, value, list);
        return this;
    }

    private Node<K, V> evictLeastRecentyUsed() {
        Node<K, V> node = list.removeLast().orElseThrow();
        map.remove(node.getKey());
        log.info("Evicting key: {} value: {}", node.getKey(), node.getValue());
        return node;
    }

    @Override
    public Optional<V> get(K key) {
        if(!map.containsKey(key)) return Optional.empty();
        Node<K, V> node = map.get(key);
        node.removeFromList();
        list.addFirst(node);
        log.info("Returning key: {} value: {}", node.getKey(), node.getValue());
        return Optional.of(node.getValue());
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public LRUCache<K, V> clear() {
        list.clear();
        map.clear();
        return this;
    }
}
