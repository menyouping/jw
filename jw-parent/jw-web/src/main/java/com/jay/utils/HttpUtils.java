package com.jay.utils;

import java.net.URI;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jw.util.JwUtils;

/**
 * @see http://www.oschina.net/code/snippet_54371_1515
 *
 */
public class HttpUtils {
 
    private static Logger LOGGER = LoggerFactory.getLogger(HttpUtils.class);
    private static HttpClient hc = new DefaultHttpClient();
 
    /**
     * Get请求
     * @param url
     * @param params
     * @return
     */
    public static String get(String url, List<NameValuePair> params) {
        String body = null;
        try {
            // Get请求
            HttpGet httpget = new HttpGet(url);
            // 设置参数
            if(!JwUtils.isEmpty(params)) {
                String str = EntityUtils.toString(new UrlEncodedFormEntity(params));
                httpget.setURI(new URI(url + (url.contains("?") ? "&" : "?") + str));
            }
            // 发送请求
            HttpResponse httpresponse = hc.execute(httpget);
            // 获取返回数据
            HttpEntity entity = httpresponse.getEntity();
            body = EntityUtils.toString(entity, "UTF-8");
            if (entity != null) {
                entity.consumeContent();
            }
        } catch (Exception e) {
            LOGGER.error("Error raised when send get request to "+ url, e);
        }
        return body;
    }
 
    /**
     * Post请求
     * @param url
     * @param params
     * @return
     */
    public static String post(String url, List<NameValuePair> params) {
        String body = null;
        try {
            // Post请求
            HttpPost httppost = new HttpPost(url);
            // 设置参数
            httppost.setEntity(new UrlEncodedFormEntity(params));
            // 发送请求
            HttpResponse httpresponse = hc.execute(httppost);
            // 获取返回数据
            HttpEntity entity = httpresponse.getEntity();
            body = EntityUtils.toString(entity);
            if (entity != null) {
                entity.consumeContent();
            }
        } catch (Exception e) {
            LOGGER.error("Error raised when send post request to "+ url, e);
        }
        return body;
    }
 
}
