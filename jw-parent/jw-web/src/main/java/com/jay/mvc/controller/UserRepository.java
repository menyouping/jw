package com.jay.mvc.controller;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import com.jw.aop.annotation.Query;
import com.jw.db.JwConnection;
import com.jw.db.SQLUtils;
import com.jw.util.JwUtils;
import com.jw.web.bind.annotation.Repository;

@Repository
public class UserRepository {

    public ResultSet find(JwConnection conn, int i) {
        Map<String, Object> params = JwUtils.newHashMap();
        params.put("id", 1);

        return SQLUtils.query(conn, "select * from sys_user where id = :id", params);
    }

    @Query("select * from sys_user where id = ?")
    public List<UserDto> findById(int id) {
        return null;
    }
}
