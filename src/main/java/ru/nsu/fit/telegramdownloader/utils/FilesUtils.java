package ru.nsu.fit.telegramdownloader.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FilesUtils {
    static public void mkDir(String name) {
        File f = new File(name);
        f.mkdir();
    }

    static public long getFileSize(String filename) throws IOException {
        return Files.size(Paths.get(filename));
    }
}
