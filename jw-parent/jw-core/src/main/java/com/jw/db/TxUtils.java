package com.jw.db;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.Logger;

/**
 * Transaction Util
 * 
 * @author Jay Zhang
 * 
 */
public class TxUtils {
    private static Logger LOGGER = Logger.getLogger(TxUtils.class);

    public static <T> T call(TxCallable<T> callable) {
        return call(callable, DatabaseManager.getDefaultDBName());
    }

    public static <T> T call(TxCallable<T> callable, String dbName) {
        T result = null;
        Database db = null;
        Connection connection = null;
        try {
            db = DatabaseManager.getDB(dbName);
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

    public static <T> void run(TxRunnable runnable) {
        run(runnable, DatabaseManager.getDefaultDBName());
    }

    public static <T> void run(TxRunnable runnable, String dbName) {
        Database db = null;
        Connection connection = null;
        try {
            db = DatabaseManager.getDB(dbName);
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