package org.apache.synapse.integration.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class TestUtils {
    public static String getCurrentDir() {
        return System.getProperty("user.dir") + File.separator;
    }

    public static String getFileBody(File filePath) throws IOException {

        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(filePath);
            int c;
            StringBuilder stringBuilder = new StringBuilder();
            while ((c = fileInputStream.read()) != -1) {
                stringBuilder.append(c);
            }
            String content = stringBuilder.toString();
            content = content.replace("\n", "").replace("\r", "");

            return content;
        } finally {
            if (fileInputStream != null) {
                fileInputStream.close();
            }
        }
    }
}
