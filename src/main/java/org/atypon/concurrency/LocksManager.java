package org.atypon.concurrency;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LocksManager {

    private static final Map<String, ReentrantReadWriteLock> locks = new HashMap<>();

    private LocksManager() {
    }

    public static LocksManager getInstance() {
        return LocksManagerHolder.INSTANCE;
    }

    public ReentrantReadWriteLock getLock(String key) {
        if (!locks.containsKey(key)) {
            locks.put(key, new ReentrantReadWriteLock());
        }
        return locks.get(key);
    }


    private static class LocksManagerHolder {
        private static final LocksManager INSTANCE = new LocksManager();
    }

}
