package com.jw.util;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class PkgUtils {

    @SuppressWarnings({ "rawtypes" })
    public static <A extends Annotation> Set<Class<?>> findClazesByAnnotation(String pkgName, Class<A> annoClaze)
            throws Exception {
        if(StringUtils.isEmpty(pkgName))
            return null;
        Set<Class<?>> list = new LinkedHashSet<Class<?>>();
        Set<Class<?>> clazes = getClazes(pkgName);
        for (Class claze : clazes) {
            if (claze.isAnnotation())
                continue;
            if (JwUtils.isAnnotated(claze, annoClaze)) {
                list.add(claze);
            }
        }
        return list;
    }

    /**
     * 从包package中获取所有的Class
     * 
     */
    public static Set<Class<?>> getClazes(String pkg) {

        Set<Class<?>> classes = new LinkedHashSet<Class<?>>();
        boolean recursive = true;
        String pkgDirName = pkg.replace('.', '/');
        try {
            URL url = PkgUtils.class.getClassLoader().getResource(pkgDirName);
            if(url == null)
                return classes;
            String protocol = url.getProtocol();
            if ("file".equals(protocol)) {// 如果是以文件的形式保存在服务器上
                String filePath = URLDecoder.decode(url.getFile(), "UTF-8");// 获取包的物理路径
                findClazesByFile(pkg, filePath, recursive, classes);
            } else if ("jar".equals(protocol)) {// 如果是jar包文件
                JarFile jar = ((JarURLConnection) url.openConnection()).getJarFile();
                findClazesByJar(pkg, jar, recursive, classes);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return classes;
    }

    public static void findClazesByFile(String pkgName, String pkgPath, final boolean recursive, Set<Class<?>> clazes) {
        File dir = new File(pkgPath);
        if (!dir.exists() || !dir.isDirectory())
            return;
        File[] dirfiles = dir.listFiles(new FileFilter() {
            public boolean accept(File file) {
                return (recursive && file.isDirectory()) || (file.getName().endsWith(".class"));
            }
        });
        String className;
        Class<?> claze;
        for (File file : dirfiles) {
            if (file.isDirectory()) {
                findClazesByFile(pkgName + "." + file.getName(), file.getAbsolutePath(), recursive, clazes);
            } else {
                className = file.getName();
                className = className.substring(0, className.length() - 6);
                claze = getClaze(pkgName, className);
                if (claze != null) {
                    clazes.add(claze);
                }
            }
        }
    }

    public static void findClazesByJar(String pkgName, JarFile jar, final boolean recursive, Set<Class<?>> classes) {
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
            if (name.startsWith(packageDirName)) {
                int index = name.lastIndexOf('/');
                if (index > -1) {// 以"/"结尾 是一个包
                    pkgName = name.substring(0, index).replace('/', '.');// 获取包名
                                                                         // 把"/"替换成"."
                }
                if ((index > -1) || recursive) {
                    if (name.endsWith(".class") && !jarEntry.isDirectory()) {
                        className = name.substring(pkgName.length() + 1, name.length() - 6);// 去掉后面的".class"
                                                                                            // 获取真正的类名
                        claze = getClaze(pkgName, className);
                        if (claze != null) {
                            classes.add(claze);
                        }
                    }
                }
            }
        }
    }

    public static Class<?> getClaze(String pkgName, String clazeName) {
        try {
            return Thread.currentThread().getContextClassLoader().loadClass(pkgName + '.' + clazeName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
