package com.jay.mvc.domain;

import com.jw.domain.annotation.Table;

@Table(name = "dict_meaning")
public class Meaning {

    private long id;
    private long wordId;
    private String type;
    private String v;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getWordId() {
        return wordId;
    }

    public void setWordId(long wordId) {
        this.wordId = wordId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getV() {
        return v;
    }

    public void setV(String v) {
        this.v = v;
    }

}
