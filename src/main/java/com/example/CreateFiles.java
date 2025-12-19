package com.example;

import java.io.File;
import java.io.FileWriter;

public class CreateFiles {

           public static void createFile(String filePath, String content) throws Exception {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(content);
        }
    }

    public static void deleteFile(String filePath) {
        new File(filePath).delete();
    }
}
