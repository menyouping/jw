package com.jw.db;

import java.util.Collection;

public interface DBManager {

    public String getDefaultDBName();

    public DB getDefaultDB();

    public DB getDB(String dbName);

    public Collection<String> getDBNames();

}
