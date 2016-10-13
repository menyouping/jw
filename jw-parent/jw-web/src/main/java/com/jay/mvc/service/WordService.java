package com.jay.mvc.service;

import java.util.List;
import java.util.Map;

import com.jay.mvc.domain.Word;
import com.jay.mvc.repository.WordRepository;
import com.jw.domain.annotation.Autowired;
import com.jw.web.bind.annotation.Service;

@Service
public class WordService {

    @Autowired
    WordRepository repository;

    /**
     * 检索单个单词或词组
     * @param keyword
     * @return
     */
    public Word find(String keyword) {
        return repository.find(keyword);
    }

    /**
     * 检索单词
     * 
     * @param keyword
     * @return
     */
    @SuppressWarnings("rawtypes")
    public List<Map> findWords(String keyword) {
        return repository.find(0, keyword + "%");
    }

    /**
     * 检索词组
     * 
     * @param keyword
     * @return
     */
    @SuppressWarnings("rawtypes")
    public List<Map> findGroups(String keyword) {
        return repository.find(1, keyword + "%");
    }

}
