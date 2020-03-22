package com.jw.util;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PkgUtils {
    private static final Logger LOG = LoggerFactory.getLogger(PkgUtils.class);

    private static final String FILE_PROTOCOL = "file";
    private static final String JAR_PROTOCOL = "jar";

    /**
     * 查找被某个标记标注的类
     * 
     * @param pkgName   支持多folder, 以";"分隔
     * @param annoClaze
     * @return
     * @throws Exception
     */
    @SuppressWarnings({ "rawtypes" })
    public static <A extends Annotation> Set<Class<?>> findClazesByAnnotation(String pkgName, Class<A> annoClaze)
            throws Exception {
        ArgumentChecker.notEmpty(pkgName);

        Set<Class<?>> set = new LinkedHashSet<Class<?>>();

        Set<Class<?>> clazes = null;
        String[] pkgs = pkgName.split("\\s*;\\s*");
        for (String pkg : pkgs) {
            if (pkg.isEmpty()) {
                continue;
            }
            clazes = findClazes(pkg);
            if (clazes == null) {
                continue;
            }
            for (Class claze : clazes) {
                if (claze.isAnnotation()) {
                    continue;
                }
                if (AnnotationUtils.isAnnotated(claze, annoClaze)) {
                    set.add(claze);
                }
            }
        }
        return set;
    }

    /**
     * 从包package中获取所有的Class
     * 
     */
    public static Set<Class<?>> findClazes(String pkg) {
        String pkgDirName = pkg.replace('.', '/');
        try {
            URL url = PkgUtils.class.getClassLoader().getResource(pkgDirName);
            if (url == null) {
                return null;
            }
            String protocol = url.getProtocol();
            Set<Class<?>> classes = new LinkedHashSet<Class<?>>();
            if (FILE_PROTOCOL.equals(protocol)) {// 如果是以文件的形式保存在服务器上
                String filePath = URLDecoder.decode(url.getFile(), "UTF-8");// 获取包的物理路径
                findClazesByFile(pkg, filePath, classes);
            } else if (JAR_PROTOCOL.equals(protocol)) {// 如果是jar包文件
                JarFile jar = ((JarURLConnection) url.openConnection()).getJarFile();
                findClazesByJar(pkg, jar, classes);
            }
            return classes;
        } catch (IOException e) {
            LOG.error(String.format("查找目录%s下的类失败", pkg), e);
        }

        return null;
    }

    public static void findClazesByFile(final String pkgName, String pkgPath, Set<Class<?>> clazes) {

        Path start = Paths.get(pkgPath);
        int maxDepth = 100;
        try (Stream<Path> stream = Files.find(start, maxDepth,
                (path, attr) -> String.valueOf(path).endsWith(".class"))) {
            stream.forEach(p -> {
                Class<?> claze = loadClaze(String.valueOf(p).replace(pkgPath, pkgName + ".").replace("/", "."));
                if (claze != null) {
                    clazes.add(claze);
                }
            });
        } catch (IOException e) {
            LOG.error("遍历文件失败", e);
        }
    }

    public static void findClazesByJar(String pkgName, JarFile jar, Set<Class<?>> classes) {
        String packageDirName = pkgName.replace('.', '/');

        Enumeration<JarEntry> jarEntries = jar.entries();
        JarEntry jarEntry;
        String name, className;
        Class<?> claze;
        while (jarEntries.hasMoreElements()) {
            jarEntry = jarEntries.nextElement();
            name = jarEntry.getName();
            if (name.charAt(0) == '/') {
                name = name.substring(1);
            }
            // 如果前半部分和定义的包名相同
            if (!name.startsWith(packageDirName)) {
                continue;
            }
            int index = name.lastIndexOf('/');
            // 以"/"结尾 是一个包
            if (index > -1) {
                // 获取包名,把"/"替换成"."
                pkgName = name.substring(0, index).replace('/', '.');
            }
            // 如果可以迭代下去 并且是一个包
            if (name.endsWith(".class") && !jarEntry.isDirectory()) {
                // 去掉后面的".class" 获取真正的类名
                className = name.substring(pkgName.length() + 1, name.length() - 6);
                claze = loadClaze(pkgName, className);
                if (claze != null) {
                    classes.add(claze);
                }
            }
        }
    }

    public static Class<?> loadClaze(String pkgName, String clazeName) {
        return loadClaze(pkgName + '.' + clazeName);
    }

    public static Class<?> loadClaze(String fullClazeName) {
        try {
            if (fullClazeName.endsWith(".class")) {
                fullClazeName = fullClazeName.substring(0, fullClazeName.length() - 6);
            }
            if(LOG.isDebugEnabled()) {
                LOG.info("加载类{}", fullClazeName);
            }
            return Class.forName(fullClazeName);
        } catch (ClassNotFoundException e) {
            LOG.error(String.format("加载类%s失败", fullClazeName), e);
        }
        return null;
    }
}
