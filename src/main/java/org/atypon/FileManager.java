package org.atypon;

import java.io.File;

public class FileManager {
    private FileManager() {}

    public static boolean deleteFile(String path) {
        File file = new File(path);
        return file.delete();
    }


}
