package com.jay.mvc.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.jay.mvc.dto.HttpRequestDto;
import com.jay.utils.ActionResult;
import com.jay.utils.HttpUtils;
import com.jw.domain.annotation.Autowired;
import com.jw.util.StringUtils;
import com.jw.web.bind.annotation.Controller;
import com.jw.web.bind.annotation.ModelAttribute;
import com.jw.web.bind.annotation.RequestMapping;
import com.jw.web.bind.annotation.RequestMethod;
import com.jw.web.bind.annotation.ResponseBody;

@Controller
public class HttpController {

    @Autowired
    HttpServletRequest request;

    @RequestMapping(value = "/http")
    public String http() {
        return "http";
    }

    @RequestMapping(value = "/http/send", method = RequestMethod.POST)
    @ResponseBody
    public Object httpSend(@ModelAttribute("dto") HttpRequestDto dto) {
        ActionResult result = dto.validate();
        if (!result.isOK()) {
            return result;
        }

        String contentType = "";
        if ("JSON".equals(dto.getContentType())) {
            contentType = "application/json";
        } else if ("XML".equals(dto.getContentType())) {
            contentType = "application/xml;charset=utf-8";
        }

        Map<String, String> headers = null;
        if (!StringUtils.isEmpty(dto.getHeader())) {
            headers = new HashMap<>();
            String[] list = dto.getHeader().split("\\s*;\\s*");
            for (String header : list) {
                if (header.isEmpty()) {
                    continue;
                }
                String[] kv = header.split("\\s*=\\s*", 2);
                if (kv != null && kv.length == 2) {
                    headers.put(kv[0], kv[1]);
                }
            }
        }

        if ("PUT".equals(dto.getMethod())) {
            result = HttpUtils.put(dto.getUrl(), dto.getBody(), headers, contentType);
            return result;
        } else if ("POST".equals(dto.getMethod())) {
            result = HttpUtils.post(dto.getUrl(), dto.getBody(), headers, contentType);
            return result;
        }
        return ActionResult.fail("不应该到达此处");
    }

}
