package com.jay.mvc.service;

import java.util.List;
import java.util.Map;

import com.jay.mvc.domain.Word;
import com.jay.mvc.repository.WordRepository;
import com.jw.cache.CacheManagerFactory;
import com.jw.db.EntityUtils;
import com.jw.domain.annotation.Autowired;
import com.jw.web.bind.annotation.Service;

@Service
public class WordService {

    @Autowired
    WordRepository repository;
    
    public void setRepository(WordRepository repository) {
        this.repository = repository;
    }

    /**
     * 检索单个单词或词组
     * 
     * @param keyword
     * @return
     */
//    @Cacheable(value = "defaultCache", prefix = "word.")
    public Word find(String keyword) {
        return repository.find(keyword);
    }

    public String find(Integer id) {
        return repository.find(id);
    }
    
    public Map<String,Object> findV(Integer id) {
        return repository.findKV(id);
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

    public void create(Word word) {
        EntityUtils.create(word);
        CacheManagerFactory.getManager().getCache("defaultCache").put("word." + word.getK(), word);
    }

    public void update(Word word) {
        repository.update(word);
    }
}
