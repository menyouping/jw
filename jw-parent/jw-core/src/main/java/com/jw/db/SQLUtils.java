package com.jw.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

public class SQLUtils {

    private static final Logger LOGGER = Logger.getLogger(SQLUtils.class);

    public static int executeUpdate(String dbName, String sql, Object[] params) {
        DB db = DBManager.getDB(dbName);
        return executeUpdate(db, sql, params);
    }

    public static int executeUpdate(String sql, Object[] params) {
        DB db = DBManager.getDefaultDB();
        return executeUpdate(db, sql, params);
    }

    public static int executeUpdate(DB db, String sql, Object[] params) {
        Connection conn = db.getConnection();
        int result = executeUpdate(conn, sql, params);
        db.releaseConnection(conn);
        return result;
    }

    public static int executeUpdate(Connection conn, String sql, Object[] params) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql);
        } catch (SQLException e) {
            LOGGER.error("Prepare statement failed", e);
            return 0;
        }

        if (params != null) {
            int len = params.length;
            for (int i = 1; i <= len; i++) {
                try {
                    ps.setObject(i, params[i]);
                } catch (SQLException e) {
                    LOGGER.error("Error raised when set value in PreparedStatement", e);
                    return 0;
                }
            }
        }
        int i = 0;
        try {
            i = ps.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("Error raised when execute sql : " + sql, e);
        }
        return i;
    }

    public static ResultSet executeQuery(String dbName, String sql, Object[] params) {
        DB db = DBManager.getDB(dbName);
        return executeQuery(db, sql, params);
    }

    public static ResultSet executeQuery(String sql, Object[] params) {
        DB db = DBManager.getDefaultDB();
        return executeQuery(db, sql, params);
    }

    public static ResultSet executeQuery(DB db, String sql, Object[] params) {
        Connection conn = db.getConnection();
        ResultSet rs = executeQuery(conn, sql, params);
        db.releaseConnection(conn);
        return rs;
    }

    public static ResultSet executeQuery(Connection conn, String sql, Object[] params) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql);
        } catch (Exception e) {
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
                    LOGGER.error("Error raised when set value in PreparedStatement", e);
                    return null;
                }
            }
        }
        ResultSet rs = null;
        try {
            rs = ps.executeQuery();
        } catch (SQLException e) {
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
