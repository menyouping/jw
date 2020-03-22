package com.jw.web.context;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.jw.util.AnnotationUtils;
import com.jw.util.CollectionUtils;
import com.jw.util.ConfigUtils;
import com.jw.util.PkgUtils;
import com.jw.util.StringUtils;
import com.jw.web.bind.annotation.Controller;
import com.jw.web.bind.annotation.PathVariable;
import com.jw.web.bind.annotation.RequestMapping;
import com.jw.web.bind.annotation.RequestMethod;

public class UrlMappingRegistry {

    private static final Logger LOG = LoggerFactory.getLogger(UrlMappingRegistry.class);

    /**
     * store common url
     */
    private static final Multimap<String, UrlMapping> urlMap = ArrayListMultimap.create();
    /**
     * Store url with path variable
     */
    private static final List<UrlMapping> pathUrls = CollectionUtils.newArrayList();

    private UrlMappingRegistry() {
    }

    public static void init() {
        try {
            String pkg = ConfigUtils.getProperty("package.scan");
            Set<Class<?>> clazes = PkgUtils.findClazesByAnnotation(pkg, Controller.class);
            register(clazes);
        } catch (Exception e) {
            LOG.error("注册Controller失败", e);
        }
    }

    public static void register(Collection<Class<?>> controllers) {
        if (controllers == null || controllers.isEmpty()) {
            return;
        }
        for (Class<?> controller : controllers) {
            register(controller);
        }
    }

    public static void register(Class<?> controller) {
        String[] urls = null;
        String clazeUrl = "";

        if (AnnotationUtils.isAnnotated(controller, RequestMapping.class)) {
            urls = controller.getAnnotation(RequestMapping.class).value();
            if (CollectionUtils.isEmpty(urls) || urls.length > 1) {
                LOG.error("类{}的RequestMapping配置不正确.", controller.getName());
                return;
            } else {
                clazeUrl = urls[0];
            }
        }

        Method[] methods = controller.getMethods();
        UrlMapping urlMapping = null;

        boolean flag = false;
        for (Method method : methods) {
            if (!AnnotationUtils.isAnnotated(method, RequestMapping.class)) {
                continue;
            }
            method.setAccessible(true);
            urls = method.getAnnotation(RequestMapping.class).value();
            flag = false;
            for (String url : urls) {
                urlMapping = buildUrlMapping(method, clazeUrl, url);
                urlMap.put(clazeUrl + url, urlMapping);
                flag = true;
            }
            if (!flag) {
                urlMapping = new UrlMapping(method);
                urlMap.put(clazeUrl + method.getName(), urlMapping);
            }
        }
    }

    public static UrlMapping buildUrlMapping(Method method, String clazeUrl, String url) {
        UrlMapping urlMapping = new UrlMapping(method);

        url = StringUtils.replaceConfig(url);
        if (!StringUtils.PATTERN_PATH_VARIABLE.matcher(url).find()) {
            urlMapping.setUrl(clazeUrl + url);
            urlMapping.setUrlExpression("^" + clazeUrl + url + "$");
            pathUrls.add(urlMapping);
            return urlMapping;
        }

        Annotation[][] methodParamAnnos = method.getParameterAnnotations();
        if (CollectionUtils.isEmpty(methodParamAnnos)) {
            return urlMapping;
        }

        Map<String, Integer> pathVariableMap = CollectionUtils.newHashMap();
        Annotation[] paramAnnos = null;
        Class<?>[] paramTypes = method.getParameterTypes();
        Class<?> paramType;
        int index = 1;// the regex index in url
        String key, exp = null;
        String urlExpression = url;
        for (int i = 0; i < methodParamAnnos.length; i++) {
            paramAnnos = methodParamAnnos[i];
            for (Annotation anno : paramAnnos) {
                if (PathVariable.class != anno.annotationType()) {
                    continue;
                }
                paramType = paramTypes[i];
                key = ((PathVariable) anno).value();
                pathVariableMap.put(key, index);
                if (String.class.equals(paramType)) {
                    exp = "([^/]+)";
                } else if (Integer.TYPE.equals(paramType) || Integer.class.equals(paramType)
                        || Long.TYPE.equals(paramType) || Long.class.equals(paramType)) {
                    exp = "([-+]?\\d+)";
                } else if (Double.TYPE.equals(paramType) || Double.class.equals(paramType)
                        || Float.TYPE.equals(paramType) || Float.class.equals(paramType)) {
                    exp = "([-+]?\\d+(\\.\\d+)?)";
                    index++;
                } else if (Boolean.TYPE.equals(paramType) || Boolean.class.equals(paramType)) {
                    exp = "(true|false|y|n|yes|no|1|0)";
                }
                index++;
                urlExpression = urlExpression.replace("{" + key + "}", exp);
                break;
            }
        }
        urlMapping.setUrl(clazeUrl + url);
        urlMapping.setUrlExpression("^" + clazeUrl + urlExpression + "$");
        urlMapping.setPathVariableMap(pathVariableMap);
        pathUrls.add(urlMapping);
        return urlMapping;
    }

    /**
     * 根据用户请求的路径，查找对应的Java class和method
     * 
     * @param requestMethod GET,POST,PUT,DELETE, ...
     * @param path
     * @return
     */
    public static UrlMapping match(String requestMethod, String path) {
        Collection<UrlMapping> urlMappings = urlMap.get(path);
        if (CollectionUtils.isEmpty(urlMappings)) {
            if (!CollectionUtils.isEmpty(pathUrls)) {
                for (UrlMapping urlMapping : pathUrls) {
                    if (urlMapping.getUrlPattern().matcher(path).find()) {
                        urlMappings = urlMap.get(urlMapping.getUrl());
                        break;
                    }
                }
            }
        }
        if (CollectionUtils.isEmpty(urlMappings)) {
            return null;
        }
        RequestMethod[] allowedActions;
        for (UrlMapping urlMapping : urlMappings) {
            allowedActions = urlMapping.getMethod().getAnnotation(RequestMapping.class).method();

            if (CollectionUtils.isEmpty(allowedActions))
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
