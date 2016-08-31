package com.jw.db;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;

import com.jw.util.ConfigUtils;
import com.jw.util.StringUtils;

public class DatabaseManager {
    private static final Logger LOGGER = Logger.getLogger(DatabaseManager.class);

    private static Properties config = ConfigUtils.getProperties("db");
    
    public static final String DEFAULT_DB = "defaultDb";

    private static final DatabaseManager manager = new DatabaseManager();

    private Map<String, Database> dbPools = new Hashtable<String, Database>();

    private DatabaseManager() {
        registerDrivers();
        connectDatabases();
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

    private void connectDatabases() {
        Set<String> props = config.stringPropertyNames();
        for (String prop : props) {
            if (!prop.endsWith("url"))
                continue;
            String dbName = prop.replace("url", "");
            String url = getProperty(dbName, "url");
            String user = getProperty(dbName, "user");
            String pwd = getProperty(dbName, "password");
            String str_max = getProperty(dbName, "max.connection", "20");
            int maxConn = 20;
            try {
                maxConn = Integer.valueOf(str_max);
            } catch (NumberFormatException e) {
                LOGGER.error("The max db connection amount of " + dbName + " is invalid，set to default value 20.");
                maxConn = 20;
            }
            if(StringUtils.isEmpty(dbName)) {
                dbName = DEFAULT_DB;
            }
            Database db = new Database(dbName, url, user, pwd, maxConn);
            dbPools.put(dbName, db);
            LOGGER.info("Successfully to create the db connection to " + dbName);
        }
    }

    public static String getDefaultDBName() {
        return DEFAULT_DB;
    }
    
    public static Database getDefaultDB() {
        return manager.dbPools.values().iterator().next();
    }

    public static Database getDB(String dbName) {
        return StringUtils.isEmpty(dbName) ? DatabaseManager.getDefaultDB() : manager.dbPools.get(dbName);
    }

    private static String getProperty(String dbName, String key) {
        if (dbName.isEmpty()) {
            return config.getProperty(key);
        }
        return config.getProperty(dbName + "." + key);
    }

    private static String getProperty(String pollName, String key, String defaultValue) {
        if (pollName.isEmpty()) {
            return config.getProperty(key, defaultValue);
        }
        return config.getProperty(pollName + "." + key, defaultValue);
    }

}
