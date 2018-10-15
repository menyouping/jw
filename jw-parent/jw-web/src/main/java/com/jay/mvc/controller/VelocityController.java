package com.jay.mvc.controller;

import java.io.StringWriter;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import com.jay.mvc.dto.VelocityDto;
import com.jw.util.JwUtils;
import com.jw.util.StringUtils;
import com.jw.web.bind.annotation.Controller;
import com.jw.web.bind.annotation.ModelAttribute;
import com.jw.web.bind.annotation.RequestMapping;
import com.jw.web.bind.annotation.ResponseBody;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.tools.generic.MathTool;
import org.apache.velocity.tools.generic.NumberTool;

@RequestMapping(value = "/velocity")
@Controller
public class VelocityController {

    @RequestMapping(value = "/")
    public String velocity() {
        return "velocity";
    }

    @RequestMapping(value = "/render")
    @ResponseBody
    public Object render(@ModelAttribute("dto") VelocityDto dto) {
        Map<String, Object> result = JwUtils.newHashMap();

        if (StringUtils.isEmpty(dto.getPayload())) {
            result.put("status", 200);
            result.put("message", "SUCCESS");
            result.put("data", "");
            return result;
        }

        JSONObject json = new JSONObject();
        if (!StringUtils.isEmpty(dto.getParams())) {
            try {
                json = JSONObject.parseObject(dto.getParams());
            } catch (Exception e) {
                result.put("status", 0);
                result.put("message", "参数格式有误！请用JSON工具校验。");
                return result;
            }
        }

        VelocityContext context = new VelocityContext();
        for (String key : json.keySet()) {
            context.put(key, json.get(key));
        }
        context.put("number", new NumberTool());
        context.put("math", new MathTool());

        VelocityEngine ve = new VelocityEngine();
        ve.init();

        StringWriter writer = new StringWriter();
        try {
            ve.evaluate(context, writer, "", dto.getPayload());
        } catch (Exception e) {
            result.put("status", 0);
            result.put("message", e.getMessage());
            return result;
        }
        

        result.put("status", 200);
        result.put("message", "SUCCES");
        result.put("data", writer.toString());
        return result;
    }

}
