package leetcode.examples.list;

import java.util.*;

public class LinkedHashMapLRUCache<K, V> implements LRUCache<K, V> {

    private int size;
    private LinkedHashMap<K, V> map;

    public LinkedHashMapLRUCache(int size) {
        this.size = size;
        map = new LinkedHashMap<>(size);
    }

    @Override
    public LRUCache<K, V> put(K key, V value) {
        return this;
    }

    @Override
    public Optional<V> get(K key) {
        return Optional.empty();
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public LRUCache<K, V> clear() {
        return this;
    }
}
