package com.jw.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

public class DBUtils {

    private static final Logger LOGGER = Logger.getLogger(DBUtils.class);

    public static int executeUpdate(String dbName, String sql, Object... params) {
        Connection conn = DatabaseManager.getDB(dbName).getConnection();
        return executeUpdate(dbName, conn, sql, params);
    }

    public static int executeUpdate(String sql, Object... params) {
        Database db = DatabaseManager.getDefaultDB();
        return executeUpdate(db, sql, params);
    }

    protected static int executeUpdate(Database db, String sql, Object... params) {
        PreparedStatement ps = null;
        Connection conn = null;
        try {
            conn = db.getConnection();
            ps = conn.prepareStatement(sql);
        } catch (SQLException e) {
            db.releaseConnection(conn);
            LOGGER.error("Prepare statement failed", e);
            return 0;
        }

        if (params != null) {
            int len = params.length;
            for (int i = 1; i <= len; i++) {
                try {
                    ps.setObject(i, params[i]);
                } catch (SQLException e) {
                    db.releaseConnection(conn);
                    LOGGER.error("Error raised when set value in PreparedStatement", e);
                }
            }
        }
        int i = 0;
        try {
            i = ps.executeUpdate();
        } catch (SQLException e) {
            db.releaseConnection(conn);
            LOGGER.error("Error raised when execute sql : " + sql, e);
        }
        return i;
    }

    public static ResultSet executeQuery(String dbName, String sql, Object... params) {
        Database db = DatabaseManager.getDB(dbName);
        return executeQuery(dbName, db, sql, params);
    }

    public static ResultSet executeQuery(String sql, Object... params) {
        Database db = DatabaseManager.getDefaultDB();
        return executeQuery(db, sql, params);
    }

    protected static ResultSet executeQuery(Database db, String sql, Object... params) {
        PreparedStatement ps = null;
        Connection conn = null;
        try {
            conn = db.getConnection();
            ps = conn.prepareStatement(sql);
        } catch (Exception e) {
            db.releaseConnection(conn);
            LOGGER.error("Prepare statement failed", e);
            return null;
        }
        // 设置参数
        if (params != null) {
            int len = params.length;
            for (int i = 1; i <= len; i++) {
                try {
                    ps.setObject(i, params[i]);
                } catch (SQLException e) {
                    db.releaseConnection(conn);
                    LOGGER.error("Error raised when set value in PreparedStatement", e);
                    return null;
                }
            }
        }
        ResultSet rs = null;
        try {
            rs = ps.executeQuery();
        } catch (SQLException e) {
            db.releaseConnection(conn);
            LOGGER.error("Error raised when querying with sql : " + sql, e);
            return null;
        }
        return rs;
    }

    public static void release(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                System.out.println("Error raised when close rs.");
                return;
            }
        }
    }
}
