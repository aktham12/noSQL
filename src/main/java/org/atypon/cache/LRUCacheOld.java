package org.atypon.cache;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;


public class LRUCacheOld<K, V> extends LinkedHashMap<K, V> implements Cache<K, V> {
    private final Map<K, V> cacheMap;

    private final int maxSize;

    public LRUCacheOld(int capacity) {
        cacheMap = Collections.synchronizedMap(new LinkedHashMap<>(capacity, 0.75f, true));
        this.maxSize = capacity;
    }

    @Override
    public V get(Object o) {
        return cacheMap.get(o);
    }


    @Override
    public V put(K k, V v) {


        return cacheMap.put(k, v);
    }


    @Override
    public V remove(Object o) {
        return cacheMap.remove(o);
    }

    @Override
    public boolean containsKey(Object o) {
        return cacheMap.containsKey(o);
    }

    @Override
    public int size() {
        return 0;
    }

    @Override public Set<K> keySet() {
        return cacheMap.keySet();
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return this.size() > maxSize;
    }
}
