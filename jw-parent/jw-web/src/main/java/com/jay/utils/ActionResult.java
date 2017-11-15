package com.jay.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

/**
 * 
 * @author James Zhang
 *
 */
public class ActionResult {

    public static final Integer OK = 200;
    public static final Integer FAIL = 0;
    private int status = OK;
    /**
     * For error use
     */
    private String message = "ok";
    /**
     * For success use, give user extra information
     */
    private String notice = "";
    private Object data;
    private Map<String, Object> extendedData;

    public int getStatus() {
        return status;
    }

    public boolean isOK() {
        return OK == status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public Object getData() {
        return data;
    }

    @JSONField(serialize = false)
    @JsonIgnore
    public JSONObject getJSONData() {
        if (data == null) {
            return null;
        }
        return JSON.parseObject(data.toString());
    }

    @JSONField(serialize = false)
    @JsonIgnore
    @SuppressWarnings("unchecked")
    public <T> T getValue(String key) {
        if (data instanceof String) {
            return JsonUtils.get(JSON.parseObject((String) data), key);
        }
        if (data instanceof JSONObject) {
            return JsonUtils.get((JSONObject) (data), key);
        }
        if (data instanceof Map) {
            return JsonUtils.get(new JSONObject((Map<String, Object>) data), key);
        }
        return null;
    }

    @JSONField(serialize = false)
    @JsonIgnore
    @SuppressWarnings("unchecked")
    public <T> T getDataEntity() {
        return (T) data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @JSONField(serialize = false)
    @JsonIgnore
    public Object getExtendedData() {
        return extendedData;
    }

    @SuppressWarnings("unchecked")
    public void setExtendedData(Object value) {
        if (value == null) {
            this.extendedData = null;
        } else {
            if (value instanceof Map) {
                extendedData = (Map<String, Object>) value;
            } else {
                if (this.extendedData == null) {
                    extendedData = new HashMap<String, Object>(3);
                }
                extendedData.put(String.valueOf(extendedData.size()), value);
            }
        }
    }

    public boolean hasExtendedData(String key) {
        if (extendedData == null)
            return false;
        return extendedData.containsKey(key);
    }

    @SuppressWarnings("unchecked")
    public <T> T getExtendedData(String key) {
        if (extendedData == null)
            return null;
        return (T) extendedData.get(key);
    }

    public void setExtendedData(String key, Object value) {
        if (extendedData == null) {
            extendedData = new HashMap<String, Object>(3);
        }
        extendedData.put(key, value);
    }

    public Object removeExtendedData(String key) {
        if (extendedData == null) {
            return null;
        }
        return extendedData.remove(key);
    }

    public void onFail(String message) {
        status = ActionResult.FAIL;
        this.message = message;
    }

    public void onFail(JSONObject data) {
        status = ActionResult.FAIL;
        this.data = data;
    }

    public void onFail(List<?> data) {
        status = ActionResult.FAIL;
        this.data = data;
    }

    public static ActionResult fail(String message) {
        ActionResult result = new ActionResult();
        result.onFail(message);
        return result;
    }
    
    public static void responseFail(HttpServletResponse resp, String message) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");
        resp.getWriter().print(ActionResult.fail(message));
    }

    public void onOK(Object data) {
        status = ActionResult.OK;
        message = "ok";
        this.data = data;
    }

    public void onOK(Object data, String notice) {
        status = ActionResult.OK;
        message = "ok";
        this.notice = notice;
        this.data = data;
    }

    public static ActionResult ok(Object data) {
        ActionResult result = new ActionResult();
        result.onOK(data);
        return result;
    }

    public static ActionResult ok(Object data, String notice) {
        ActionResult result = new ActionResult();
        result.onOK(data, notice);
        return result;
    }

    public static ActionResult ok() {
        return new ActionResult();
    }

    public static ActionResult build(int status, String message, Object data) {
        ActionResult result = new ActionResult();
        result.status = status;
        result.message = message;
        result.data = data;
        return result;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    public static ActionResult parse(String data) {
        return JSON.parseObject(data, ActionResult.class);
    }

    public static void main(String[] args) {
        ActionResult result = ActionResult.fail("1111");
        result.setData(new Object());
        System.out.println(result.toString());
    }
}

