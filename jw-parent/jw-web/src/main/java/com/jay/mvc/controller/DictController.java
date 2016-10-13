package com.jay.mvc.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.jay.mvc.domain.Word;
import com.jay.mvc.service.WordService;
import com.jay.web.QueryResult;
import com.jw.domain.annotation.Autowired;
import com.jw.ui.Model;
import com.jw.util.StringUtils;
import com.jw.web.bind.annotation.Controller;
import com.jw.web.bind.annotation.PathVariable;
import com.jw.web.bind.annotation.RequestMapping;
import com.jw.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = { "/dict" })
public class DictController extends AbstractController {

    @Autowired
    WordService wordService;

    @RequestMapping(value = { "", "/" })
    public String index() {
        return "index";
    }
    
    @RequestMapping("/{keyword}")
    public String dict(Model model, @PathVariable("keyword") String keyword) {
        if (!StringUtils.isBlank(keyword)) {
            Word word = wordService.find(keyword.trim());
            model.addAttribute("model", word);
        }
        model.addAttribute("keyword", keyword);
        return "dict";
    }

    @SuppressWarnings("rawtypes")
    @RequestMapping("/suggest/{keyword}")
    @ResponseBody
    public QueryResult suggest(@PathVariable("keyword") String keyword) {
        QueryResult result = new QueryResult();
        result.setMessage("SUCCESS");
        if (StringUtils.isBlank(keyword)) {
            result.setResult(Collections.emptyList());
        } else if (keyword.trim().contains(" ")) {
            List<Map> list = wordService.findGroups(keyword.trim());
            result.setResult(list);
        } else {
            List<Map> list = wordService.findWords(keyword.trim());
            result.setResult(list);
        }
        return result;
    }

}
