package com.jay.mvc.repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import com.jay.mvc.dto.UserDto;
import com.jw.aop.annotation.Query;
import com.jw.db.SQLUtils;
import com.jw.util.CollectionUtils;
import com.jw.web.bind.annotation.Repository;

@Repository
public class UserRepository {

    public ResultSet find(Connection conn, int i) {
        Map<String, Object> params = CollectionUtils.newHashMap();
        params.put("id", 1);

        return SQLUtils.query(conn, "select * from sys_user where id = :id", params);
    }

    @Query("select * from sys_user where id = ?")
    public List<UserDto> findById(int id) {
        return null;
    }
}
