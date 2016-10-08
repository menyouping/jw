package com.jay.mvc.service;

import java.util.List;

import com.jay.mvc.repository.WordRepository;
import com.jw.domain.annotation.Autowired;
import com.jw.web.bind.annotation.Service;

@Service
public class WordService {

    @Autowired
    WordRepository repository;

    public List<String> find(String keyword) {
        return repository.find(keyword);
    }

}
