package com.jay.scratcher;

import java.util.Map;

public interface IScratcher {

    String request(String url, Map<String, Object> params);
    
    <T> T extract(String content);
    
    
    <T> T scratch(String url, Map<String, Object> params);

}
