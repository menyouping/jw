package com.jay.mvc.controller;

public enum Gender {
    MALE(1),FEMALE(0),UNKNOWN(99);
    
    int constant;
    Gender(int constant) {
        this.constant = constant;
    }
}
