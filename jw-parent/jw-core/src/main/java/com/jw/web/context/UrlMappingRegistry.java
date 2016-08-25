package com.jw.web.context;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.jw.util.ConfigUtils;
import com.jw.util.JwUtils;
import com.jw.util.PkgUtils;
import com.jw.web.bind.annotation.Controller;
import com.jw.web.bind.annotation.RequestMapping;

public class UrlMappingRegistry {

    private static final Logger LOGGER = Logger.getLogger(UrlMappingRegistry.class);
    private static final Map<String, UrlMapping> urlMap = new ConcurrentHashMap<String, UrlMapping>();

    private UrlMappingRegistry() {
    }
    
    public static void init() {
        Set<Class<?>> clazes = null;

        try {
            clazes = PkgUtils.findClazesByAnnotation(ConfigUtils.getProperty("package.scan"), Controller.class);
            register(clazes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void register(Collection<Class<?>> controllers) {
        if (controllers == null || controllers.isEmpty())
            return;
        for (Class<?> controller : controllers) {
            register(controller);
        }
    }

    public static void register(Class<?> controller) {
        String[] urls = null;
        String clazeUrl = "";

        if (JwUtils.isAnnotated(controller, RequestMapping.class)) {
            urls = controller.getAnnotation(RequestMapping.class).value();
            if (urls == null || urls.length == 0 || urls.length > 1) {
                LOGGER.error(new Exception("RequestMapping on class " + controller.getSimpleName() + " is invalid."));
                return;
            } else {
                clazeUrl = urls[0];
            }
        }

        Method[] methods = controller.getMethods();
        UrlMapping urlMapping = null;
        for (Method method : methods) {
            if (!JwUtils.isAnnotated(method, RequestMapping.class))
                continue;
            urls = method.getAnnotation(RequestMapping.class).value();
            urlMapping = new UrlMapping(controller, method);
            for (String url : urls) {
                urlMap.put(clazeUrl + url, urlMapping);
            }
        }
    }

    public static UrlMapping match(String path) {
        int index = path.indexOf("?");
        if (index > -1) {
            path = path.substring(0, index);
        }

        return urlMap.get(path);
    }
}
