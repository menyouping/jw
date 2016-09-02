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
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletRequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtils.class);

    public static List<File> findFiles(final String fileExtension) {
        List<File> files = new LinkedList<File>();
        try {
            URL url = FileUtils.class.getClassLoader().getResource("/");
            if (url == null)
                return files;
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

    /**
     * @see http://www.oschina.net/code/snippet_698737_13402
     * @param request
     * @return
     * @throws IOException
     */
    public static Map<String, Object> uploadFiles(HttpServlet servlet, HttpServletRequest request) {
        Map<String, Object> map = JwUtils.newHashMap();
        Map<String, String> fileMap = JwUtils.newHashMap();
        map.put("file", fileMap);

        DiskFileItemFactory factory = new DiskFileItemFactory();// 创建工厂
        factory.setSizeThreshold(1024 * 1024 * 30);// 设置最大缓冲区为30M

        // 设置缓冲区目录
        String savePath = servlet.getServletContext().getRealPath("/WEB-INF/temp");
        factory.setRepository(new File(savePath));

        FileUpload upload = new FileUpload(factory);// 获得上传解析器
        upload.setHeaderEncoding("UTF-8");// 解决上传文件名乱码
        
        try {
            String targetFolderPath = servlet.getServletContext().getRealPath("/WEB-INF/" + ConfigUtils.getProperty("web.files.upload.folder"));
            File targetFolder = new File(targetFolderPath);// 目标文件夹
            if(!targetFolder.exists()) {
                targetFolder.mkdir();
            }
            
            List<FileItem> fileItems = upload.parseRequest(new ServletRequestContext(request));// 解析请求体
            for (FileItem fileItem : fileItems) {
                if (fileItem.isFormField()) {// 判断是普通表单项还是文件上传项
                    String name = fileItem.getFieldName();// 表单名
                    String value = fileItem.getString("UTF-8");// 表单值
                    map.put(name, value);
                } else {// 文件上传项
                    String fileName = fileItem.getName();// 获取文件名
                    if (StringUtils.isEmpty(fileName))// 判断是否上传了文件
                        continue;

                    // 截取文件名
                    int index = fileName.lastIndexOf("/");
                    if (index > -1) {
                        fileName = fileName.substring(index);
                    }

                    // 检查文件是否允许上传
                    index = fileName.lastIndexOf(".");
                    if (index > -1 && index < fileName.length() - 1) {
                        String extension = fileName.substring(index + 1).toLowerCase();
                        if (!ConfigUtils.getString("web.files.upload.extension").contains(extension)) {
                            LOGGER.warn("The file {} is not allowed to upload.", fileName);
                            continue;
                        }
                    }

                    // 生成唯一文件名，保留原文件名
                    String newFileName = UUID.randomUUID().toString();
                    
                    // 将文件内容写到服务器端
                    String targetPath = targetFolderPath + "/" + newFileName;
                    File targetFile = new File(targetPath);// 目标文件
                    targetFile.createNewFile();
                    fileItem.write(targetFile);

                    fileItem.delete();// 删除临时文件
                    fileMap.put(fileName, newFileName);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
}
