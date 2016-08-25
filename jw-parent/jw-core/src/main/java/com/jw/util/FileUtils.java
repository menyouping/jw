package com.jw.util;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class FileUtils {

    public static List<File> findFiles(final String fileExtension) {
        List<File> files = new LinkedList<File>();
        try {
            URL url = FileUtils.class.getClassLoader().getResource("/");
            String protocol = url.getProtocol();
            if ("file".equals(protocol)) {// 如果是以文件的形式保存在服务器上
                String filePath = URLDecoder.decode(url.getFile(), "UTF-8");// 获取包的物理路径
                File dir = new File(filePath);
                if (!dir.exists() || !dir.isDirectory())
                    return files;
                File[] dirfiles = dir.listFiles(new FileFilter() {
                    public boolean accept(File file) {
                        return file.getName().endsWith(fileExtension);
                    }
                });
                if (dirfiles != null && dirfiles.length > 0) {
                    files.addAll(Arrays.asList(dirfiles));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return files;
    }

    public static void copy(String file, OutputStream out) throws IOException {
        FileInputStream in = new FileInputStream(file);
        int c;
        byte buffer[] = new byte[1024];
        while ((c = in.read(buffer)) != -1) {
            for (int i = 0; i < c; i++) {
                out.write(buffer[i]);
            }
        }
        in.close();
        out.close();
    }
}
