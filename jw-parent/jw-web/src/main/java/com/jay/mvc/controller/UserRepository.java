package com.jay.mvc.controller;

import java.sql.ResultSet;
import java.util.Map;

import com.jw.db.ConnectionHolder;
import com.jw.db.SQLUtils;
import com.jw.util.JwUtils;
import com.jw.web.bind.annotation.Repository;

@Repository
public class UserRepository {

    public ResultSet find(ConnectionHolder conn, int i) {
        Map<String, Object> params = JwUtils.newHashMap();
        params.put("id", 1);
        
        return SQLUtils.query(conn, "select * from sys_user where id = :id", params );
    }
}
