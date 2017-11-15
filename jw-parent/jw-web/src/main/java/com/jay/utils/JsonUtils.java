package com.jay.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 
 * @author James Zhang
 *
 */
public class JsonUtils {

    private static Logger LOGGER = LoggerFactory.getLogger(JsonUtils.class);

    public static boolean has(JSONObject json, String fullPath) {
        try {
            String[] paths = fullPath.split("\\s*\\.\\s*");
            int len = paths.length;
            String path;
            String key;
            Object current = json;
            for (int i = 0; i < len; i++) {
                path = paths[i];
                if (path.isEmpty()) {
                    continue;
                }
                if (path.endsWith("]")) {
                    int index = path.indexOf("[");
                    key = path.substring(0, index);
                    current = ((JSONObject) current).get(key);
                    path = path.substring(index + 1);
                    String[] indice = path.replace("[", "").split("\\]");
                    for (String indx : indice) {
                        if (indx.isEmpty()) {
                            continue;
                        }
                        int i_indx = Integer.valueOf(indx);
                        if (i_indx >= ((JSONArray) current).size()) {
                            return false;
                        }
                        if (i_indx < 0 && ((JSONArray) current).size() + i_indx < 0) {
                            return false;
                        }
                        if (i_indx < 0) {
                            i_indx = ((JSONArray) current).size() + i_indx;
                        }
                        current = ((JSONArray) current).get(i_indx);
                    }
                } else {
                    key = path;
                    if (!((JSONObject) current).containsKey(key)) {
                        return false;
                    }
                    current = ((JSONObject) current).get(key);
                }
            }
            return true;
        } catch (Exception e) {
            LOGGER.error("Error raised when extract the object " + json + ", the path is " + fullPath, e);
            return false;
        }
    }

    public static <T> T get(String json, String fullPath) {
        return get(JSON.parseObject(json), fullPath);
    }
    
    @SuppressWarnings("unchecked")
    public static <T> T get(JSONObject json, String fullPath) {
        try {
            String[] paths = fullPath.split("\\s*\\.\\s*");
            int len = paths.length;
            String path;
            String key;
            Object current = json;
            for (int i = 0; i < len; i++) {
                path = paths[i];
                if (path.isEmpty()) {
                    continue;
                }
                if (path.endsWith("]")) {
                    int index = path.indexOf("[");
                    key = path.substring(0, index);
                    current = ((JSONObject) current).get(key);
                    path = path.substring(index + 1);
                    String[] indice = path.replace("[", "").split("\\]");
                    for (String indx : indice) {
                        if (indx.isEmpty()) {
                            continue;
                        }
                        int i_indx = Integer.valueOf(indx);
                        if (i_indx < 0) {
                            i_indx = ((JSONArray) current).size() + i_indx;
                        }
                        current = ((JSONArray) current).get(i_indx);
                    }
                } else {
                    key = path;
                    current = ((JSONObject) current).get(key);
                }
            }
            return (T) current;
        } catch (Exception e) {
            LOGGER.error("Error raised when extract the object " + json + ", the path is " + fullPath, e);
            return null;
        }
    }

    public static void main(String[] args) {
        String content = "{\"user\":{\"name\":\"jay\",\"age\":30,\"favorite\":[\"篮球\",\"编程\",[{\"name\":\"吃饭\"},{\"name\":\"睡觉\"}]]}}";
        JSONObject json = JSON.parseObject(content);
        System.out.println("content is " + json.toJSONString());
        System.out.println("user.name is " + JsonUtils.get(json, "user.name"));
        System.out.println("user.age is " + JsonUtils.get(json, "user.age"));
        System.out.println("user.favorite is " + JsonUtils.get(json, "user.favorite"));
        System.out.println("user.favorite[0] is " + JsonUtils.get(json, "user.favorite[0]"));
        System.out.println("user.favorite[2][1] is " + JsonUtils.get(json, "user.favorite[2][1]"));
        System.out.println("user.favorite[2][1].name is " + JsonUtils.get(json, "user.favorite[2][1].name"));
        System.out.println("user.favorite[-1][1].name is " + JsonUtils.get(json, "user.favorite[-1][1].name"));
        System.out.println("user.favorite[-1][-1].name is " + JsonUtils.get(json, "user.favorite[-1][-1].name"));
        System.out.println("user.favorite[-1][-2].name is " + JsonUtils.get(json, "user.favorite[-1][-2].name"));
        System.out.println(
          "Is content contains user.favorite[-1][-2].name? " + JsonUtils.has(json, "user.favorite[-1][-2].name"));
    }
}
