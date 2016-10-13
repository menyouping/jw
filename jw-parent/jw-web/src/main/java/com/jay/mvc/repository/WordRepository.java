package com.jay.mvc.repository;

import java.util.List;
import java.util.Map;

import com.jay.mvc.domain.Word;
import com.jw.aop.annotation.Query;
import com.jw.web.bind.annotation.Repository;

@Repository
public class WordRepository {

    @Query("select * from dict_word where k=? limit 1")
    public Word find(String keyword) {
        return null;
    }

    @SuppressWarnings("rawtypes")
    @Query("select k,v from dict_word where type = ? and k like ? order by k asc limit 10")
    public List<Map> find(int type, String keyword) {
        return null;
    }
}
