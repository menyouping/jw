package com.jay.mvc.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import com.jay.mvc.domain.enums.Gender;
import com.jay.mvc.dto.UserDto;
import com.jay.mvc.repository.UserRepository;
import com.jw.aop.annotation.Transactional;
import com.jw.cache.annotation.CacheEvict;
import com.jw.cache.annotation.CacheKey;
import com.jw.cache.annotation.Cacheable;
import com.jw.db.EntityUtils;
import com.jw.domain.annotation.Autowired;
import com.jw.web.bind.annotation.Service;

@Service
public class UserService {

    @Autowired
    UserRepository repository;

    @Transactional
    public Object find(Connection conn, int i) {
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
        user.setGender(Gender.MALE);
        return user;
    }

    @Cacheable(value = "defaultCache", prefix = "user.", join = "#")
    public Object findUser(@CacheKey("name") String name, @CacheKey("age") int age) {
        UserDto user = new UserDto();
        user.setName("jay");
        user.setAge(30);
        user.setGender(Gender.MALE);
        return user;
    }

    @CacheEvict(value="defaultCache", prefix = "user.")
    public void deleteUser(int id) {
    }
}
