package org.atypon.io;

import org.apache.commons.io.FileUtils;


import java.io.File;
import java.io.IOException;

public class DirectoryCreator {

    private static final File MASTER_DIR = new File("databases");


    private DirectoryCreator() {

    }

    public static DirectoryCreator getInstance() {
        return DirectoryCreatorHolder.INSTANCE;
    }


    public  void createDirectory(String path) {
            File dir = new File(MASTER_DIR + "/" + path);
            if (!dir.exists()) {
                try {
                    FileUtils.forceMkdir(dir);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }


    public  File createFile(String path, String fileName) {
        File dir = new File(MASTER_DIR + "/" + path);

        this.createDirectory(dir.getPath());

        File file = new File(dir, fileName);
        if (file.exists()) {
            return file;
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return file;
    }

    public File getMasterDir() {
        return new File(MASTER_DIR.getPath());
    }

    private static class DirectoryCreatorHolder {
        private static final DirectoryCreator INSTANCE = new DirectoryCreator();
    }


}
