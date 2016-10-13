package com.jay.mvc.controller;

import java.util.Collections;
import java.util.List;

import com.jay.mvc.service.WordService;
import com.jay.web.QueryResult;
import com.jw.domain.annotation.Autowired;
import com.jw.web.bind.annotation.Controller;
import com.jw.web.bind.annotation.PathVariable;
import com.jw.web.bind.annotation.RequestMapping;
import com.jw.web.bind.annotation.ResponseBody;

@Controller
public class DictController extends AbstractController {

    @Autowired
    WordService wordService;

    @RequestMapping(value = { "/dict" })
    public String index() {
        return "index";
    }

    @RequestMapping("/dict/suggest/{keyword}")
    @ResponseBody
    public QueryResult suggest(@PathVariable("keyword") String keyword) {
        QueryResult result = new QueryResult();
        result.setMessage("SUCCESS");
        if (keyword == null || keyword.trim().isEmpty()) {
            result.setResult(Collections.emptyList());
        } else if (keyword.contains(" ")) {
            List<String> list = wordService.findWords(keyword);
            result.setResult(list);
        } else {
            List<String> list = wordService.find(keyword);
            result.setResult(list);
        }
        return result;
    }

}
