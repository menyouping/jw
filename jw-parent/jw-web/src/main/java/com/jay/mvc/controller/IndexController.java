package com.jay.mvc.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.jay.aop.annotation.Log;
import com.jay.mvc.dto.UserDto;
import com.jay.mvc.service.UserService;
import com.jw.db.JwConnection;
import com.jw.domain.annotation.Autowired;
import com.jw.domain.annotation.Value;
import com.jw.ui.Model;
import com.jw.util.JwUtils;
import com.jw.util.SessionContext;
import com.jw.validation.ValidErrors;
import com.jw.web.bind.annotation.Controller;
import com.jw.web.bind.annotation.ModelAttribute;
import com.jw.web.bind.annotation.PathVariable;
import com.jw.web.bind.annotation.RequestMapping;
import com.jw.web.bind.annotation.RequestMethod;
import com.jw.web.bind.annotation.RequestParam;
import com.jw.web.bind.annotation.ResponseBody;
import com.jw.web.bind.annotation.Valid;

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

    @RequestMapping(value = "/page/{page}")
    public String page(@PathVariable("page") String page) {
        return page;
    }

    @RequestMapping(value = "/json")
    public String json() {
        return "json";
    }

    @RequestMapping(value = "/xml")
    public String xml() {
        return "xml";
    }

    @RequestMapping(value = "/sql")
    public String sql() {
        return "sql";
    }

    /**
     * 集合
     * 
     * @return
     */
    @RequestMapping(value = "/set")
    public String set() {
        return "set";
    }
    
    @RequestMapping(value = "/template")
    public String template() {
        return "template";
    }

    @RequestMapping(value = "/compare")
    public String compare() {
        return "compare";
    }

    @RequestMapping(value = "/gitLog")
    public String gitLog() {
        return "git_log";
    }

    @RequestMapping(value = "/html5")
    public String html5() {
        return "html5";
    }
    
    @RequestMapping(value = "/velocity")
    public String velocity() {
        return "velocity";
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
        result.put("body", service.find(JwConnection.create(), 1));
        return result;
    }

    @RequestMapping(value = "/query")
    @ResponseBody
    public Object query() {
        Map<String, Object> result = JwUtils.newHashMap();
        result.put("status", 200);
        result.put("message", "SUCCESS");
        result.put("body", service.findById(1));
        return result;
    }

    @RequestMapping(value = "/save")
    @ResponseBody
    public Object save() {
        Map<String, Object> result = JwUtils.newHashMap();
        result.put("status", 200);
        result.put("message", "SUCCESS");
        return result;
    }

    @RequestMapping(value = "/cache")
    @ResponseBody
    public Object cache() {
        Map<String, Object> result = JwUtils.newHashMap();
        result.put("status", 200);
        result.put("message", "SUCCESS");
        result.put("body", service.findUser(1));
        return result;
    }

    @RequestMapping(value = "/cache2")
    @ResponseBody
    public Object cache2() {
        Map<String, Object> result = JwUtils.newHashMap();
        result.put("status", 200);
        result.put("message", "SUCCESS");
        result.put("body", service.findUser("jay", 30));
        return result;
    }

    @RequestMapping(value = "/clearCache")
    @ResponseBody
    public Object clearCache() {
        Map<String, Object> result = JwUtils.newHashMap();
        result.put("status", 200);
        result.put("message", "SUCCESS");
        service.deleteUser(1);
        return result;
    }

    @RequestMapping(value = "/valid")
    @ResponseBody
    public Object valid(@Valid @ModelAttribute("dto") UserDto dto, ValidErrors error) {
        Map<String, Object> result = JwUtils.newHashMap();
        result.put("status", 200);
        if (error == null) {
            result.put("message", "SUCCESS");
        } else {
            result.put("message", "FAILED");
            result.put("body", error.toString());
        }
        return result;
    }

}
