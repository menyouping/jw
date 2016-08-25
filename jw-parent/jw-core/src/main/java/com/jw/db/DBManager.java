package com.jw.db;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;

import com.jw.util.ConfigUtils;
import com.jw.util.StringUtils;

public class DBManager {
    private static final Logger LOGGER = Logger.getLogger(DBManager.class);

    private static Properties config = ConfigUtils.getProperties("db");

    private static final DBManager manager = new DBManager();

    private Hashtable<String, DBConnectionPool> connectionPools = new Hashtable<String, DBConnectionPool>();

    private DBManager() {
        registerDrivers();
        buildConnectionPools();
    }

    private void registerDrivers() {
        String[] driverNames = config.getProperty("drivers").trim().split("\\s*;\\s*");
        for (String driverName : driverNames) {
            try {
                Driver driver = (Driver) Class.forName(driverName).newInstance();
                DriverManager.registerDriver(driver);
                LOGGER.info("Successfully loaded the db driver of " + driverName);
            } catch (InstantiationException | IllegalAccessException e) {
                LOGGER.error("Error raised when loading db driver of " + driverName + ", pls check the properties.", e);
            } catch (ClassNotFoundException e) {
                LOGGER.error("Not found the driver of " + driverName + ", pls add the related driver lib.", e);
            } catch (SQLException e) {
                LOGGER.error("Error raised when loading db driver of " + driverName + ", pls check the driver lib.", e);
            }
        }
    }

    private void buildConnectionPools() {
        Set<String> props = config.stringPropertyNames();
        for (String prop : props) {
            if (!prop.endsWith("url"))
                continue;
            String poolName = prop.replace("url", "");
            String url = getProperty(poolName, "url");
            String user = getProperty(poolName, "user");
            String pwd = getProperty(poolName, "password");
            String str_max = getProperty(poolName, "max.connection", "20");
            int maxConn = 20;
            try {
                maxConn = Integer.valueOf(str_max);
            } catch (NumberFormatException e) {
                LOGGER.error("The max db connection amount of " + poolName + " is invalid，set to default value 20.");
                maxConn = 20;
            }
            if(StringUtils.isEmpty(poolName)) {
                poolName = "defaultDb";
            }
            DBConnectionPool pool = new DBConnectionPool(poolName, url, user, pwd, maxConn);
            connectionPools.put(poolName, pool);
            LOGGER.info("Successfully to create the db connection to " + poolName);
        }
    }

    private static DBConnectionPool getDefaultPool() {
        return manager.connectionPools.values().iterator().next();
    }

    private static DBConnectionPool getPool(String poolName) {
        return StringUtils.isEmpty(poolName) ? DBManager.getDefaultPool() : manager.connectionPools.get(poolName);
    }

    public static Connection getConnection() {
        return getDefaultPool().getConnection();
    }
    
    public static Connection getConnection(int timeout) {
        DBConnectionPool pool = DBManager.getDefaultPool();
        return pool.getConnection(timeout);
    }

    public static Connection getConnection(String poolName) {
        DBConnectionPool pool = DBManager.getPool(poolName);
        return pool.getConnection();
    }

    public static Connection getConnection(String poolName, int timeout) {
        DBConnectionPool pool = DBManager.getPool(poolName);
        return pool.getConnection(timeout);
    }

    public static void freeConnection(String poolName, Connection conn) {
        DBConnectionPool pool = DBManager.getPool(poolName);
        if (pool != null) {
            pool.freeConnection(conn);
        }
    }

    private static String getProperty(String poolName, String key) {
        if (poolName.isEmpty()) {
            return config.getProperty(key);
        }
        return config.getProperty(poolName + "." + key);
    }

    private static String getProperty(String pollName, String key, String defaultValue) {
        if (pollName.isEmpty()) {
            return config.getProperty(key, defaultValue);
        }
        return config.getProperty(pollName + "." + key, defaultValue);
    }

}
