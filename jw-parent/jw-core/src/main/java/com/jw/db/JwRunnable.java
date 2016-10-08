package com.jw.db;

import java.sql.Connection;

/**
 * 
 * @author Jay Zhang
 * 
 */
public interface JwRunnable {
    public void run(Connection connection) throws Exception;
}
