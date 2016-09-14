package com.jay.mvc.controller;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import com.jw.aop.annotation.Transaction;
import com.jw.db.JwConnection;
import com.jw.db.EntityUtils;
import com.jw.domain.annotation.Autowired;
import com.jw.web.bind.annotation.Service;

@Service
public class UserService {

    @Autowired
    UserRepository repository;
    
    @Transaction
    public Object find(JwConnection conn, int i) {
        ResultSet rs = repository.find(conn, i);
        List<Map<String, Object>> list = EntityUtils.toMaps(rs);
        return list;
    }
}
