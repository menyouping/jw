package com.jay.mvc.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.jay.mvc.service.WordService;
import com.jw.domain.annotation.Autowired;
import com.jw.util.JwUtils;
import com.jw.util.StringUtils;
import com.jw.web.bind.annotation.Controller;
import com.jw.web.bind.annotation.PathVariable;
import com.jw.web.bind.annotation.RequestMapping;
import com.jw.web.bind.annotation.ResponseBody;

@Controller
public class DictController {

    @Autowired
    WordService service;

    @RequestMapping(value = { "/dict" })
    public String index() {
        return "dict";
    }

    @RequestMapping("/dict/suggest/{keyword}")
    @ResponseBody
    public Object rest(@PathVariable("keyword") String keyword) {
        Map<String, Object> result = JwUtils.newHashMap();
        result.put("status", 200);
        result.put("message", "SUCCESS");
        if (StringUtils.isEmpty(keyword)) {
            result.put("result", Collections.emptyList());
        } else {
            List<String> list = service.find(keyword);
            result.put("result", list);
        }
        return result;
    }

}
