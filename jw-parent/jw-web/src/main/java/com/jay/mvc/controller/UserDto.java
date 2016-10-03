package com.jay.mvc.controller;

import com.jw.validation.constraints.annotation.Between;

public class UserDto {

    private String name;

    @Between(min = 1, max = 120, message = "年龄必须在1-120之间")
    private int age;
    private boolean sex;

    public UserDto() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }
}
