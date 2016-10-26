package com.jay.scratcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.jay.utils.XmlConverUtil;
import com.jw.util.StringUtils;

public class ICIBAWordScratcher extends AbstractGetScratcher {

    private static Logger LOGGER = LoggerFactory.getLogger(ICIBAWordScratcher.class);
    
    public static final String URL = "http://dict-co.iciba.com/api/dictionary.php?w={word}&key={key}";

    @SuppressWarnings("unchecked")
    @Override
    public JSONObject extract(String content) {
        try {
            String json = XmlConverUtil.xmltoJson(content);
            if(StringUtils.isEmpty(json)) {
                return null;
            }
            json = json.replace("\\n", "");
            JSONObject map = JSONObject.parseObject(json);
            map.remove("@name");
            map.remove("@num");
            map.remove("@id");
            return  map;
        } catch (Exception e) {
            LOGGER.error("Error rasised when convert xml to json, the xml is " + content, e);
        }
        return null;
    }

}
