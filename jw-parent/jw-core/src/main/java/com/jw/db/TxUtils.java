package com.jw.db;

import java.sql.Connection;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Transaction Util
 * 
 * @author Jay Zhang
 * 
 */
public class TxUtils {
    private static Logger LOGGER = LoggerFactory.getLogger(TxUtils.class);

    public static <T> T call(TxCallable<T> callable) {
        return call(DBManager.getDefaultDBName(), callable);
    }

    public static <T> T call(String dbName, TxCallable<T> callable) {
        T result = null;
        DB db = null;
        Connection connection = null;
        try {
            db = DBManager.getDB(dbName);
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
        run(DBManager.getDefaultDBName(), runnable);
    }

    public static <T> void run(String dbName, TxRunnable runnable) {
        DB db = null;
        Connection connection = null;
        try {
            db = DBManager.getDB(dbName);
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