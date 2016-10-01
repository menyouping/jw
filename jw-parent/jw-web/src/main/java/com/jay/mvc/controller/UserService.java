package com.jay.mvc.controller;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import com.jw.aop.annotation.Transaction;
import com.jw.cache.annotation.CacheEvict;
import com.jw.cache.annotation.CacheKey;
import com.jw.cache.annotation.Cacheable;
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

    public List<UserDto> findById(int i) {
        return repository.findById(i);
    }

    @Cacheable(value = "defaultCache", prefix = "user.")
    public UserDto findUser(@CacheKey("id") int id) {
        UserDto user = new UserDto();
        user.setName("jay");
        user.setAge(30);
        user.setSex(true);
        return user;
    }

    @Cacheable(value = "defaultCache", prefix = "user.", join = "#")
    public Object findUser(@CacheKey("name") String name, @CacheKey("age") int age) {
        UserDto user = new UserDto();
        user.setName("jay");
        user.setAge(30);
        user.setSex(true);
        return user;
    }

    @CacheEvict(value="defaultCache", prefix = "user.")
    public void deleteUser(int id) {
    }
}
