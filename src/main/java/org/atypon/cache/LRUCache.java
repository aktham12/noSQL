package org.atypon.cache;

import java.util.LinkedHashMap;
import java.util.Map;


public class LRUCache<K,V> extends LinkedHashMap<K,V> implements Cache<K,V>{

    private final int maxSize;

    public LRUCache(int capacity) {
        super(capacity, 0.75f, true);
        this.maxSize = capacity;
    }

    @Override
    public V get(Object o) {
        return super.get(o);
    }


    @Override
    public V put(K k, V v) {
        return super.put(k, v);
    }


    @Override
    public V remove(Object o) {
        return super.remove(o);
    }

    @Override
    public boolean containsKey(Object o) {
        return super.containsKey(o);
    }



    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return this.size() > maxSize;
    }
}
