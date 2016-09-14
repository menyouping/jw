package com.jay.mvc.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jay.aop.annotation.Log;
import com.jw.db.ConnectionHolder;
import com.jw.domain.annotation.Autowired;
import com.jw.domain.annotation.Value;
import com.jw.ui.Model;
import com.jw.util.JwUtils;
import com.jw.util.SessionContext;
import com.jw.web.bind.annotation.Controller;
import com.jw.web.bind.annotation.ModelAttribute;
import com.jw.web.bind.annotation.PathVariable;
import com.jw.web.bind.annotation.RequestMapping;
import com.jw.web.bind.annotation.RequestMethod;
import com.jw.web.bind.annotation.RequestParam;
import com.jw.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {

    @Value("${db.max.connection}")
    int maxConn;

    @Autowired
    HttpServletRequest request;

    @Autowired
    UserService service;

    @ModelAttribute
    public void init(Model model) {
        model.addAttribute("now", System.currentTimeMillis());
    }

    @RequestMapping(value = { "/", "/index" })
    public String index() {
        return "index";
    }

    @RequestMapping(value = "/{page}")
    public String page(@PathVariable("page") String page) {
        return page;
    }

    @RequestMapping("/list")
    public String list(Model model) {
        model.addAttribute("jay", "It is me.");
        return "list";
    }

    @RequestMapping("/upload-file")
    @ResponseBody
    public Object uploadFile() {
        Map<String, Object> result = JwUtils.newHashMap();
        result.put("status", 200);
        result.put("message", "SUCCESS");
        result.put("body", SessionContext.getContext().get(SessionContext.FILE_UPLOAD_PARAMETERS));
        return result;
    }

    @RequestMapping("/health-check")
    @ResponseBody
    public Object healthCheck(@RequestParam("maxConn") int maxConn) {
        Map<String, Object> result = JwUtils.newHashMap();
        result.put("status", 200);
        result.put("message", "SUCCESS");
        JSONObject body = new JSONObject();
        body.put("maxConn", maxConn);
        result.put("body", body);
        return result;
    }

    @RequestMapping("/rest/{user}/{age}")
    @ResponseBody
    public Object rest(@PathVariable("user") String user, @PathVariable("age") int age) {
        Map<String, Object> result = JwUtils.newHashMap();
        result.put("status", 200);
        result.put("message", "SUCCESS");
        JSONObject body = new JSONObject();
        body.put("user", user);
        body.put("age", age);
        result.put("body", body);
        return result;
    }

    @RequestMapping("/rest/{user}/{age}/{sex}")
    @ResponseBody
    public Object rest(@PathVariable("user") String user, @PathVariable("age") int age,
            @PathVariable("sex") Boolean sex) {
        Map<String, Object> result = JwUtils.newHashMap();
        result.put("status", 200);
        result.put("message", "SUCCESS");
        JSONObject body = new JSONObject();
        body.put("user", user);
        body.put("age", age);
        body.put("sex", sex);
        result.put("body", body);
        return result;
    }

    @Log
    @RequestMapping(value = "/model")
    @ResponseBody
    public Object model(@ModelAttribute("dto") UserDto userDto) {
        Map<String, Object> result = JwUtils.newHashMap();
        result.put("status", 200);
        result.put("message", "SUCCESS");
        result.put("body", JSONObject.parse(JSON.toJSONString(userDto)));
        return result;
    }

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    @ResponseBody
    public Object form(@ModelAttribute("dto") UserDto userDto) {
        Map<String, Object> result = JwUtils.newHashMap();
        result.put("status", 200);
        result.put("message", "SUCCESS");
        result.put("body", JSONObject.parse(JSON.toJSONString(userDto)));
        return result;
    }

    @RequestMapping(value = "/request")
    @ResponseBody
    public Object doRequest() {
        Map<String, Object> result = JwUtils.newHashMap();
        result.put("status", 200);
        result.put("message", "SUCCESS");
        result.put("body", request.getServletPath());
        return result;
    }

    @RequestMapping(value = "/tx")
    @ResponseBody
    public Object tx() {
        Map<String, Object> result = JwUtils.newHashMap();
        result.put("status", 200);
        result.put("message", "SUCCESS");
        result.put("body", service.find(ConnectionHolder.create(), 1));
        return result;
    }

}
