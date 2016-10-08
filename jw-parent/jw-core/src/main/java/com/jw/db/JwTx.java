package com.jw.db;

import java.sql.Connection;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jw.web.context.AppContext;

/**
 * Transaction Util
 * 
 * @author Jay Zhang
 * 
 */
public class JwTx {
    private static Logger LOGGER = LoggerFactory.getLogger(JwTx.class);

    private static final DBManager manager = AppContext.getBean("dbManager");
    
    public static <T> T call(JwCallable<T> callable) {
        return call(manager.getDefaultDBName(), callable);
    }

    public static <T> T call(String dbName, JwCallable<T> callable) {
        T result = null;
        DB db = null;
        Connection connection = null;
        try {
            db = manager.getDB(dbName);
            connection = db.getConnection();
            connection.setAutoCommit(false);
            result = callable.call(connection);
            connection.commit();
            connection.setAutoCommit(true);
        } catch (Throwable e) {
            rollback(connection);
            LOGGER.error("Transaction failed:", e);
            throw new RuntimeException(e);
        } finally {
            if (db != null) {
                db.releaseConnection(connection);
            }
        }

        return result;
    }

    public static <T> void run(JwRunnable runnable) {
        run(manager.getDefaultDBName(), runnable);
    }

    public static <T> void run(String dbName, JwRunnable runnable) {
        DB db = null;
        Connection connection = null;
        try {
            db = manager.getDB(dbName);
            connection = db.getConnection();
            connection.setAutoCommit(false);
            runnable.run(connection);
            connection.commit();
            connection.setAutoCommit(true);
        } catch (Throwable e) {
            rollback(connection);
            LOGGER.error("Transaction failed:", e);
            throw new RuntimeException(e);
        } finally {
            if (db != null) {
                db.releaseConnection(connection);
            }
        }
    }

    private static void rollback(Connection connection) {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.rollback();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}