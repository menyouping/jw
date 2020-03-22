package com.jw.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletRequestContext;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 文本工具类
 * 
 * @author jay
 *
 */
public class FileUtils {

    private static final Logger LOG = LoggerFactory.getLogger(FileUtils.class);

    private static final String FILE_PROTOCOL = "file";

    /**
     * 查找以fileExtension为后缀的文件
     * 
     * @param fileExtension
     * @return
     */
    public static File[] findFiles(final String fileExtension) {
        ArgumentChecker.notEmpty(fileExtension);
        try {
            URL url = FileUtils.class.getResource("/");
            if (url == null || !FILE_PROTOCOL.equals(url.getProtocol())) {
                return null;
            }
            // 获取包的物理路径
            String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
            File dir = new File(filePath);
            if (!dir.exists() || !dir.isDirectory()) {
                return null;
            }
            return dir.listFiles(file -> file.getName().endsWith(fileExtension));
        } catch (IOException e) {
            LOG.error(String.format("查找后缀为%s的文件产生了异常", fileExtension), e);
            return null;
        }
    }

    /**
     * 读取文件，输出到流中
     * 
     * @param file
     * @param out
     * @throws IOException
     */
    public static void copy(String file, OutputStream out) throws IOException {
        try (InputStream in = new FileInputStream(file)) {
            IOUtils.copy(in, out);
        } finally {
            IOUtils.closeQuietly(out);
        }
    }

    /**
     * @see http://www.oschina.net/code/snippet_698737_13402
     * @param request
     * @return
     * @throws IOException
     */
    public static Map<String, Object> uploadFiles(HttpServlet servlet, HttpServletRequest request) {
        Map<String, Object> map = CollectionUtils.newHashMap();
        Map<String, String> fileMap = CollectionUtils.newHashMap();
        map.put(FILE_PROTOCOL, fileMap);

        DiskFileItemFactory factory = new DiskFileItemFactory();// 创建工厂
        factory.setSizeThreshold(1024 * 1024 * 30);// 设置最大缓冲区为30M

        // 设置缓冲区目录
        String savePath = servlet.getServletContext().getRealPath("/WEB-INF/temp");
        factory.setRepository(new File(savePath));

        FileUpload upload = new FileUpload(factory);// 获得上传解析器
        upload.setHeaderEncoding("UTF-8");// 解决上传文件名乱码

        try {
            String targetFolderPath = servlet.getServletContext()
                    .getRealPath("/WEB-INF/" + ConfigUtils.getProperty("web.files.upload.folder"));
            File targetFolder = new File(targetFolderPath);// 目标文件夹
            if (!targetFolder.exists()) {
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
                        String ext = fileName.substring(index + 1).toLowerCase();
                        if (!ConfigUtils.getString("web.files.upload.extension").contains(";" + ext + ";")) {
                            LOG.warn("The file {} is not allowed to upload.", fileName);
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
