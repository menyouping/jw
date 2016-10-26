package com.jay.utils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.alibaba.fastjson.JSONObject;
import com.jay.mvc.domain.Word;
import com.jay.mvc.service.WordService;
import com.jay.scratcher.ICIBAWordScratcher;
import com.jay.scratcher.IScratcher;
import com.jw.db.EntityUtils;
import com.jw.util.ConfigUtils;
import com.jw.util.StringUtils;

public class ScratcherUtils {

    public static Word findWordFromIciba(String keyword) {
        String encodeKeyword = keyword.replace("\"", "%22").replace(" ", "%20").replace("?", "%3F").replace("=", "%3D");
        IScratcher scratcher = new ICIBAWordScratcher();
        String url = ICIBAWordScratcher.URL.replace("{word}", encodeKeyword).replace("{key}",
                ConfigUtils.getString("iciba.key"));
        JSONObject json = scratcher.scratch(url, null);
        if (json != null) {
            processSpecialChars(json);
            beautifyIcibaFormat(json);
            Word word = new Word();
            if (keyword.contains(" ")) {
                word.setType(1);
            }
            word.setK(keyword);
            word.setV(json.toJSONString());
            word.setAgent("iciba");
            return word;
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> processSpecialChars(Map<String, Object> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }

        Set<Entry<String, Object>> enties = map.entrySet();
        Object value = null;
        for (Entry<String, Object> entry : enties) {
            value = entry.getValue();
            if (value instanceof List) {
                entry.setValue(process((List<Object>) value));
            } else if (value instanceof JSONObject) {
                entry.setValue(processSpecialChars((Map<String, Object>) value));
            }
            // 处理特殊字符
            else if (value instanceof String) {
                entry.setValue(process(value.toString()));
            }
        }

        return map;
    }

    public static String process(String str) {
        if (str == null) {
            return null;
        }
        return str.replace("\"", "&quot;").replace("'", "&apos;");
    }

    @SuppressWarnings("unchecked")
    public static List<Object> process(List<Object> list) {
        if (list == null || list.isEmpty()) {
            return list;
        }

        int len = list.size();
        Object item = null;
        for (int i = 0; i < len; i++) {
            item = list.get(i);
            if (item instanceof List) {
                list.set(i, process((List<Object>) item));
            } else if (item instanceof Map) {
                list.set(i, processSpecialChars((Map<String, Object>) item));
            }
        }
        return list;
    }

    private static void beautifyIcibaFormat(JSONObject json) {
        convert2List(json, "pos");
        convert2List(json, "sent");
        convert2List(json, "ps");
        convert2List(json, "pron");
        if (json.containsKey("acceptation")) {
            Object acceptation = json.get("acceptation");
            if (acceptation == null) {
                json.put("acceptation", Collections.emptyList());
            } else if (acceptation instanceof String) {
                if (StringUtils.isEmpty((String) acceptation) || acceptation.toString().matches("；+")) {
                    json.put("acceptation", Collections.emptyList());
                } else {
                    json.put("acceptation", Arrays.asList(acceptation));
                }
            }
        }
    }

    private static void convert2List(JSONObject json, String field) {
        Object v = json.get(field);
        if (v == null) {
            json.put(field, Collections.emptyList());
        } else if (!(v instanceof List)) {
            json.put(field, Arrays.asList(v));
        }
    }

    public static void scratch(final WordService wordService) {
        new Thread(new Runnable() {
            public void run() {
                int count = EntityUtils.count(Word.class);
                for (int i = 1; i < count; i++) {
                    String keyword = wordService.find(i);
                    if (keyword == null) {
                        continue;
                    }
                    Word word = ScratcherUtils.findWordFromIciba(keyword);
                    if (word != null) {
                        word.setId(i);
                        wordService.update(word);
                    }
                }
            }
        }).start();
    }
}
