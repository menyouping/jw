package com.jay.mvc.dto;

public class VelocityDto {

    public VelocityDto() {
        super();
    }
    
    private String params;

    private String payload;

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

}
