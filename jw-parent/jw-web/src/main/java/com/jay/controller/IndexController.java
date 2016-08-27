package com.jay.controller;

import java.util.Map;

import com.jw.domain.annotation.Value;
import com.jw.util.JwUtils;
import com.jw.web.bind.annotation.Controller;
import com.jw.web.bind.annotation.RequestMapping;
import com.jw.web.bind.annotation.RequestParam;
import com.jw.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {

    @Value("${db.max.connection}")
    int maxConn;

    @RequestMapping(value = { "/", "/index" })
    public String index() {
        return "index";
    }

    @RequestMapping("/health-check")
    @ResponseBody
    public Object healthCheck(@RequestParam("maxConn") int maxConn) {
        Map<String, Object> result = JwUtils.newHashMap();
        result.put("status", 200);
        result.put("message", "SUCCESS");
        return result;
    }

}
