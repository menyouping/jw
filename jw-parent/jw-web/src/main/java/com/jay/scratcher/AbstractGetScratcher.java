package com.jay.scratcher;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.jay.utils.HttpUtils;
import com.jw.util.JwUtils;
import com.jw.util.StringUtils;

public abstract class AbstractGetScratcher implements IScratcher {

    @Override
    public String request(String url, Map<String, Object> params) {
        List<NameValuePair> list = null;
        if (!JwUtils.isEmpty(params)) {
            list = JwUtils.newLinkedList();
            for (Entry<String, Object> entry : params.entrySet()) {
                list.add(new BasicNameValuePair(entry.getKey(), String.valueOf(entry.getValue())));
            }
        }

        return HttpUtils.get(url, list);
    }

    public abstract <T> T extract(String content);

    @Override
    public <T> T scratch(String url, Map<String, Object> params) {
        String content = request(url, params);
        if (!StringUtils.isEmpty(content)) {
            return extract(content);
        }
        return null;
    }

}
