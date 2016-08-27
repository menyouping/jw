package com.jay.controller;

import java.util.Map;

import com.jw.util.JwUtils;
import com.jw.web.bind.annotation.Controller;
import com.jw.web.bind.annotation.RequestMapping;
import com.jw.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {

    @RequestMapping(value = { "/", "/index" })
    public String index() {
        return "index";
    }
    
    @RequestMapping("/health-check")
    @ResponseBody
    public Object healthCheck() {
        Map<String, Object> result = JwUtils.newHashMap();
        result.put("status", 200);
        result.put("message","SUCCESS");
        return result;
    }
    
    @RequestMapping()
    @ResponseBody
    public Object healthCheck2() {
        Map<String, Object> result = JwUtils.newHashMap();
        result.put("status", 200);
        result.put("message","SUCCESS");
        return result;
    }
    
    @RequestMapping({"","/h2"})
    @ResponseBody
    public Object healthCheck3() {
        Map<String, Object> result = JwUtils.newHashMap();
        result.put("status", 200);
        result.put("message","SUCCESS");
        return result;
    }

}
