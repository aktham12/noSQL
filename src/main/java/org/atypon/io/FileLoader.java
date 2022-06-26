package org.atypon.io;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class FileLoader {
    private FileLoader() { }

    public static FileLoader getInstance() {
        return FileLoaderHolder.INSTANCE;
    }

    public  List<File> loadDirectories(File parent) {
        File[] files = Objects.requireNonNull(parent.listFiles());
        List<File> directories = new ArrayList<>();

        for (File file : files) {
            if (file.isDirectory()) {
                directories.add(file);
            }
        }
        return directories;

    }

    public  List<File> loadFiles(File parent) {
        File[] files = Objects.requireNonNull(parent.listFiles());
        List<File> files1 = new ArrayList<>();

        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(".json")) {
                files1.add(file);
            }
        }
        return files1;
    }
    private static class FileLoaderHolder {
        private static final FileLoader INSTANCE = new FileLoader();
    }

}
