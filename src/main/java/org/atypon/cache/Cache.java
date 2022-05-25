package org.atypon.cache;


public interface Cache<K,V> {
    V put(K k, V v);
    V remove(Object o);
     V get(Object o);

    boolean containsKey(Object key);

    int size();






}
