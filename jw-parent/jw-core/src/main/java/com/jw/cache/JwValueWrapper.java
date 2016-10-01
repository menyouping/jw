package com.jw.cache;

public class JwValueWrapper implements ValueWrapper {
    public static final ValueWrapper VOID = new JwValueWrapper(null);

    private Object value;

    private long validTime = 0L;

    public JwValueWrapper(Object value) {
        this.value = value;
    }

    public JwValueWrapper(Object value, long validTime) {
        this.value = value;
        this.validTime = validTime;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public long getValidTime() {
        return validTime;
    }

}
