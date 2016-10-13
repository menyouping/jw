package com.jay.mvc.repository;

import java.util.List;

import com.jw.aop.annotation.Query;
import com.jw.web.bind.annotation.Repository;

@Repository
public class WordRepository {

    @Query("select k from dict_word where type = ? and k like ? order by k asc limit 10")
    public List<String> find(int type, String keyword) {
        return null;
    }
}
