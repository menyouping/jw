package com.jw.web.context;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSONObject;

public class UrlMapping {

    private boolean hasPathVariable = false;

    private Pattern urlPattern = null;

    private String url;

    private Method method;

    /**
     * key: path variable name, value: path variable regex index in urlPattern
     */
    private Map<String, Integer> pathVariableMap = null;

    public UrlMapping(Method method) {
        super();
        this.method = method;
    }

    public Class<?> getClaze() {
        return method.getDeclaringClass();
    }

    public Method getMethod() {
        return method;
    }

    public boolean hasPathVariable() {
        return hasPathVariable;
    }

    public void setUrlExpression(String urlExpression) {
        this.hasPathVariable = true;
        this.urlPattern = Pattern.compile(urlExpression);
    }

    public Pattern getUrlPattern() {
        return urlPattern;
    }

    @Override
    public String toString() {
        JSONObject map = new JSONObject();
        map.put("claze", getClaze());
        map.put("method", method.getName());
        if (hasPathVariable) {
            map.put("hasPathVariable", hasPathVariable);
            map.put("urlPattern", urlPattern == null ? "" : urlPattern.pattern());
            map.put("url", url);
            map.put("pathVariableMap", pathVariableMap);
        }
        return map.toJSONString();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, Integer> getPathVariableMap() {
        return pathVariableMap;
    }

    public void setPathVariableMap(Map<String, Integer> pathVariableMap) {
        this.pathVariableMap = pathVariableMap;
    }

    public String getPathVariable(String realUrl, String pathVariableName) {
        if (!hasPathVariable)
            return null;
        Matcher m = urlPattern.matcher(realUrl);
        if (m.find()) {
            int index = pathVariableMap.get(pathVariableName);
            return m.groupCount() >= index ? m.group(index) : null;
        }
        return null;
    }
}
