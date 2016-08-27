package com.jw.web.context;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Set;

import org.apache.log4j.Logger;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.jw.util.ConfigUtils;
import com.jw.util.JwUtils;
import com.jw.util.PkgUtils;
import com.jw.web.bind.annotation.Controller;
import com.jw.web.bind.annotation.RequestMapping;
import com.jw.web.bind.annotation.RequestMethod;

public class UrlMappingRegistry {

    private static final Logger LOGGER = Logger.getLogger(UrlMappingRegistry.class);
    private static final Multimap<String, UrlMapping> urlMap = ArrayListMultimap.create();

    private UrlMappingRegistry() {
    }

    static {
        Set<Class<?>> clazes = null;

        try {
            clazes = PkgUtils.findClazesByAnnotation(ConfigUtils.getProperty("package.scan"), Controller.class);
            register(clazes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void init() {

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

        boolean flag = false;
        for (Method method : methods) {
            if (!JwUtils.isAnnotated(method, RequestMapping.class))
                continue;
            if (!method.isAccessible()) {
                LOGGER.warn(
                        String.format("The method %s in %s is not public.", method.getName(), controller.getName()));
                continue;
            }
            urls = method.getAnnotation(RequestMapping.class).value();
            urlMapping = new UrlMapping(controller, method);
            flag = false;
            for (String url : urls) {
                if (url.isEmpty()) {
                    LOGGER.warn(String.format("The @RequestMapping value of %s in %s is invalid.", method.getName(),
                            controller.getName()));
                    continue;
                }
                urlMap.put(clazeUrl + url, urlMapping);
                flag = true;
            }
            if (!flag) {
                urlMap.put(clazeUrl + method.getName(), urlMapping);
            }
        }
    }

    /**
     * 
     * @param requestMethod
     *            GET,POST,PUT,DELETE, ...
     * @param path
     * @return
     */
    public static UrlMapping match(String requestMethod, String path) {
        Collection<UrlMapping> urlMappings = urlMap.get(path);
        if (JwUtils.isEmpty(urlMappings))
            return null;

        RequestMethod[] allowedActions;
        for (UrlMapping urlMapping : urlMappings) {
            allowedActions = urlMapping.getMethod().getAnnotation(RequestMapping.class).method();

            if (JwUtils.isEmpty(allowedActions))
                return urlMapping;

            for (RequestMethod allowedAction : allowedActions) {
                if (allowedAction.name().equals(requestMethod)) {
                    return urlMapping;
                }
            }
        }
        return null;
    }
}
