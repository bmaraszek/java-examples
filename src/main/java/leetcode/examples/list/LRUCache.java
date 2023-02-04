package leetcode.examples.list;

import java.util.Optional;

public interface LRUCache<K, V> {
    LRUCache<K, V> put(K key, V value);
    Optional<V> get(K key);
    int size();
    boolean isEmpty();
    LRUCache<K, V> clear();
}
