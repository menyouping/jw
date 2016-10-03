package com.jw.util;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.jw.ui.Model;

/**
 * 
 * @author jay
 *
 */
public class SessionContext implements java.io.Serializable {

    private static final long serialVersionUID = -8726882324373214664L;

    public final static String REQUEST = "request";
    public final static String RESPONSE = "response";
    public final static String SESSION = "session";
    public final static String FILE_UPLOAD_PARAMETERS = "fileUploadParameters";
    public final static String MODEL = "model";
    public final static String VALID_ERRORS = "validErrors";
    public final static String REQUEST_URL = "requestUrl";

    private final static ThreadLocal<SessionContext> context = new ThreadLocal<SessionContext>();

    private Map<String, Object> map = new HashMap<String, Object>();

    public static SessionContext buildContext() {
        context.set(new SessionContext());
        return context.get();
    }

    public static void setContext(SessionContext ctx) {
        context.set(ctx);
    }

    public static SessionContext getContext() {
        return context.get();
    }

    public static void removeContext() {
        context.remove();
    }

    public SessionContext() {

    }

    public static HttpServletRequest getRequest() {
        return (HttpServletRequest) getContext().map.get(REQUEST);
    }

    public static HttpServletResponse getResponse() {
        return (HttpServletResponse) getContext().map.get(RESPONSE);
    }

    public static HttpSession getSession() {
        return (HttpSession) getContext().map.get(SESSION);
    }

    public static Model getModel() {
        return (Model) getContext().map.get(MODEL);
    }

    public boolean containsKey(String key) {
        return map.containsKey(key);
    }

    public void put(String key, Object value) {
        map.put(key, value);
    }

    public SessionContext set(String key, Object value) {
        map.put(key, value);
        return this;
    }

    public Object get(String key) {
        return map.get(key);
    }
    
    public Object get(String key, Object defaultValue) {
        if (map.containsKey(key))
            return map.get(key);

        return defaultValue;
    }

    public String getString(String key) {
        return (String) map.get(key);
    }

    public String getString(String key, String defaultValue) {
        return map.containsKey(key) ? getString(key) : defaultValue;
    }

    public boolean getBoolean(String key) {
        return (Boolean) map.get(key);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return map.containsKey(key) ? getBoolean(key) : defaultValue;
    }

    public int getInt(String key) {
        return (Integer) map.get(key);
    }

    public int getInt(String key, int defaultValue) {
        return map.containsKey(key) ? getInt(key) : defaultValue;
    }
}
