package com.jay.mvc.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.jay.mvc.domain.Word;
import com.jay.mvc.service.WordService;
import com.jay.utils.ScratcherUtils;
import com.jay.web.QueryResult;
import com.jw.domain.annotation.Autowired;
import com.jw.ui.Model;
import com.jw.util.StringUtils;
import com.jw.web.bind.annotation.Controller;
import com.jw.web.bind.annotation.PathVariable;
import com.jw.web.bind.annotation.RequestMapping;
import com.jw.web.bind.annotation.ResponseBody;

@Controller
public class DictController extends AbstractController {

    @Autowired
    WordService wordService;

    @RequestMapping("/dict/{keyword}")
    public String dict(Model model, @PathVariable("keyword") String keyword) {
        model.addAttribute("keyword", keyword);
        return "dict";
    }
    
    @RequestMapping("/dict/translate/{keyword}")
    @ResponseBody
    public QueryResult translate(Model model, @PathVariable("keyword") String keyword) {
        QueryResult result = new QueryResult();
        Word word = null;
        if (!StringUtils.isBlank(keyword)) {
            word = wordService.find(keyword.trim());
            if (word == null || StringUtils.isEmpty(word.getV())) {
                Word newWord = ScratcherUtils.findWordFromIciba(keyword);
                if (newWord != null) {
                    if(word == null) {
                        wordService.create(newWord);
                        word = newWord;
                    } else {
                        word.setV(newWord.getV());
                        word.setAgent(newWord.getAgent());
                        wordService.update(word);
                    }
                }
            }
        }
        if(word != null) {
            result.setMessage("SUCCESS");
            result.setResult(word.getV());
        } else {
            result.setStatus(0);
            result.setMessage("FAILED");
        }
        return result;
    }

    @SuppressWarnings("rawtypes")
    @RequestMapping("/dict/suggest/{keyword}")
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
    
    @RequestMapping("/dict/scratch/iciba")
    @ResponseBody
    public QueryResult scratch() {
        QueryResult result = new QueryResult();
        result.setMessage("SUCCESS");
        ScratcherUtils.scratch(wordService);
        return result;
    }

}
