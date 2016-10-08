package com.jay.mvc.dto;

import com.jay.mvc.domain.enums.Gender;
import com.jw.validation.constraints.annotation.Between;

public class UserDto {

    private String name;

    @Between(min = 1, max = 120, message = "年龄必须在1-120之间")
    private int age;
    private Gender gender = Gender.UNKNOWN;

    private String[] hobbies;

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

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String[] getHobbies() {
        return hobbies;
    }

    public void setHobbies(String[] hobbies) {
        this.hobbies = hobbies;
    }

}
