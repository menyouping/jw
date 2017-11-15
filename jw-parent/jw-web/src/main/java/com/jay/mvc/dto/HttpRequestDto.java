package com.jay.mvc.dto;

import com.jay.utils.ActionResult;
import com.jw.util.StringUtils;
import com.jw.validation.constraints.annotation.NotEmpty;

public class HttpRequestDto {

    public HttpRequestDto() {
        super();
    }

    @NotEmpty
    private String url;

    @NotEmpty
    private String method;

    @NotEmpty
    private String contentType;

    private String header;

    @NotEmpty
    private String body;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public ActionResult validate() {
        if (StringUtils.isEmpty(this.getUrl())) {
            return ActionResult.fail("请设置请求地址!");
        }
        if (StringUtils.isEmpty(this.getMethod())) {
            return ActionResult.fail("请设置请求方法!");
        }
        if (!("PUT".equals(this.getMethod()) || "POST".equals(this.getMethod()))) {
            return ActionResult.fail("请求方法不支持" + this.getMethod() + "!");
        }
        if (StringUtils.isEmpty(this.getContentType())) {
            return ActionResult.fail("请设置请求方式!");
        }
        if (!("XML".equals(this.getContentType()) || "JSON".equals(this.getContentType()))) {
            return ActionResult.fail("请求方式不支持" + this.getContentType() + "!");
        }
        if (StringUtils.isEmpty(this.getBody())) {
            return ActionResult.fail("请设置请求报文!");
        }
        return ActionResult.ok();
    }

}
