package com.jay.utils;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.CollectionUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jw.util.SessionContext;

/**
 * 
 * @author James Zhang
 *
 */
@SuppressWarnings("deprecation")
public class HttpUtils {

    private static Logger LOGGER = LoggerFactory.getLogger(HttpUtils.class);

    public static CloseableHttpClient buildHttpClient() {
        HttpClientBuilder builder = HttpClientBuilder.create().useSystemProperties();
        builder.setDefaultRequestConfig(RequestConfig.custom().build());
        CloseableHttpClient httpClient = builder.build();
        return httpClient;
    }

    private static void closeHttpClient(CloseableHttpClient httpClient) {
        try {
            if (httpClient != null) {
                httpClient.close();
            }
        } catch (Exception e) {
            LOGGER.error("httpclient close exception", e);
        }
    }

    public static ActionResult get(String url) {
        return get(url, null, null);
    }

    public static ActionResult get(String url, Map<String, String> headers) {
        return get(url, null, headers);
    }

    private static boolean logDetail() {
        SessionContext ctx = SessionContext.getContext();
        return ctx == null || !ctx.containsKey("LOG_HTTP_DETAIL") || ctx.getBoolean("LOG_HTTP_DETAIL");
    }

    private static void logRequest(String content) {
        if (logDetail()) {
            LOGGER.info("The payload: " + content);
        }
    }
    
    private static void logResponse(int code, String body) {
        if (logDetail()) {
            LOGGER.info("Received response, the code is  " + code
                    + ", the content is" + body);
        } else {
            LOGGER.info("Received response, the code is  " + code);
        }
    }

    public static ActionResult get(String url, List<NameValuePair> params, Map<String, String> headers) {
        ActionResult result = new ActionResult();
        CloseableHttpClient httpClient = buildHttpClient();
        HttpGet request = null;
        try {
            request = new HttpGet(url);
            if (!CollectionUtils.isEmpty(params)) {
                String str = EntityUtils.toString(new UrlEncodedFormEntity(params));
                request.setURI(new URI(url + "?" + str));
            }
            if (headers != null) {
                for (Entry<String, String> entry : headers.entrySet()) {
                    request.addHeader(entry.getKey(), entry.getValue());
                }
            }

            LOGGER.info("Send request: " + request.getURI());
            HttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();
            String body = EntityUtils.toString(entity, "UTF-8");
            if (entity != null) {
                entity.consumeContent();
            }
            int code = response.getStatusLine().getStatusCode();
            logResponse(code, body);
            if (code == HttpStatus.SC_OK) {
                result.setData(body);
            } else {
                result.setStatus(ActionResult.FAIL);
                result.setMessage(body);
            }
        } catch (Exception e) {
            result.setStatus(ActionResult.FAIL);
            result.setMessage(e.getMessage());
            LOGGER.error("Failed to receive response:", e);
        } finally {
            closeHttpClient(httpClient);
        }

        return result;
    }

    public static ActionResult post(String url, String content, String contentType) {
        return post(url, content, null, contentType);
    }

    public static ActionResult post(String url, String content, Map<String, String> headers, String contentType) {
        ActionResult result = new ActionResult();
        CloseableHttpClient httpClient = buildHttpClient();
        HttpPost request = null;
        try {
            request = new HttpPost(url);

            StringEntity payload = new StringEntity(content, "UTF-8");
            payload.setContentType(contentType);
            payload.setContentEncoding("UTF-8");
            request.setEntity(payload);
            if (headers != null) {
                for (Entry<String, String> entry : headers.entrySet()) {
                    request.addHeader(entry.getKey(), entry.getValue());
                }
            }

            LOGGER.info("Send request: " + request.getURI());
            logRequest(content);
            HttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();
            String body = EntityUtils.toString(entity, "UTF-8");
            if (entity != null) {
                entity.consumeContent();
            }
            int code = response.getStatusLine().getStatusCode();
            logResponse(code, body);
            if (code == HttpStatus.SC_OK) {
                result.setData(body);
            } else {
                result.setStatus(ActionResult.FAIL);
                result.setMessage("The status code is " + code + ", the content is" + body);
            }
        } catch (Exception e) {
            result.setStatus(ActionResult.FAIL);
            result.setMessage(e.getMessage());
            LOGGER.error("Failed to receive response:", e);
        } finally {
            closeHttpClient(httpClient);
        }
        return result;
    }

    public static ActionResult put(String url, String content, String contentType) {
        return put(url, content, null, contentType);
    }

    public static ActionResult put(String url, String content, Map<String, String> headers, String contentType) {
        ActionResult result = new ActionResult();
        CloseableHttpClient httpClient = buildHttpClient();
        HttpPut request = null;
        try {
            request = new HttpPut(url);

            StringEntity payload = new StringEntity(content, "UTF-8");
            payload.setContentType(contentType);
            payload.setContentEncoding("UTF-8");
            request.setEntity(payload);
            if (headers != null) {
                for (Entry<String, String> entry : headers.entrySet()) {
                    request.addHeader(entry.getKey(), entry.getValue());
                }
            }

            LOGGER.info("Send request: " + request.getURI());
            logRequest(content);
            HttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();
            String body = EntityUtils.toString(entity, "UTF-8");
            if (entity != null) {
                entity.consumeContent();
            }
            int code = response.getStatusLine().getStatusCode();
            logResponse(code, body);
            if (code == HttpStatus.SC_OK) {
                result.setData(body);
            } else {
                result.setStatus(ActionResult.FAIL);
                result.setMessage("The status code is " + code + ", the content is" + body);
            }
        } catch (Exception e) {
            result.setStatus(ActionResult.FAIL);
            result.setMessage(e.getMessage());
            LOGGER.error("Failed to receive response:", e);
        } finally {
            closeHttpClient(httpClient);
        }
        return result;
    }

}

