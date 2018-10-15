package com.jay.mvc.controller;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import com.jay.mvc.dto.O3Dto;
import com.jay.utils.O3Utils;
import com.jw.util.JwUtils;
import com.jw.util.StringUtils;
import com.jw.web.bind.annotation.Controller;
import com.jw.web.bind.annotation.ModelAttribute;
import com.jw.web.bind.annotation.RequestMapping;
import com.jw.web.bind.annotation.ResponseBody;

@RequestMapping(value = "/o3")
@Controller
public class O3Controller {

    /**
     * MSS O3
     * 
     * @return
     */
    @RequestMapping(value = "/")
    public String o3() {
        return "o3";
    }

    @RequestMapping(value = "/sign")
    @ResponseBody
    public Object o3Sign(@ModelAttribute("dto") O3Dto o3Dto) {
        Map<String, Object> result = JwUtils.newHashMap();

        if (!StringUtils.isEmpty(o3Dto.getBody())) {
            result.put("status", 200);
            result.put("message", "SUCCESS");
            result.put("body", O3Utils.calcSign(JSONObject.parseObject(o3Dto.getBody()), o3Dto.getSecretKey()));
            return result;
        }
        result.put("status", 0);
        result.put("message", "FAILED");
        result.put("body", "{}");
        return result;
    }

}
