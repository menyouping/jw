package com.jw.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

public class DBUtils {

    private static final Logger LOGGER = Logger.getLogger(DBUtils.class);

    public static int executeUpdate(String poolName, String sql, Object... params) {
        Connection conn = DBManager.getConnection(poolName);
        return executeUpdate(poolName, conn, sql, params);
    }

    public static int executeUpdate(String sql, Object... params) {
        Connection conn = DBManager.getConnection(10);
        return executeUpdate("", conn, sql, params);
    }

    protected static int executeUpdate(String poolName, Connection conn, String sql, Object... params) {
        PreparedStatement ps = null;
        try {
            // 注册事件
            assert conn != null;
            ps = conn.prepareStatement(sql);
        } catch (SQLException e) {
            DBManager.freeConnection(poolName, conn);
            LOGGER.error("Prepare statement failed", e);
            return 0;
        }
        // 设置参数
        if (params != null) {
            int len = params.length;
            for (int i = 1; i <= len; i++) {
                try {
                    ps.setObject(i, params[i]);
                } catch (SQLException e) {
                    DBManager.freeConnection(poolName, conn);
                    LOGGER.error("Error raised when set value in PreparedStatement", e);
                }
            }
        }
        int i = 0;
        try {
            i = ps.executeUpdate();
        } catch (SQLException e) {
            DBManager.freeConnection(poolName, conn);
            LOGGER.error("Error raised when execute sql : " + sql, e);
        }
        return i;
    }

    public static ResultSet executeQuery(String poolName, String sql, Object... params) {
        Connection conn = DBManager.getConnection(poolName ,10);
        return executeQuery(poolName, conn, sql, params);
    }

    public static ResultSet executeQuery(String sql, Object... params) {
        Connection conn = DBManager.getConnection(10);
        return executeQuery("", conn, sql, params);
    }

    protected static ResultSet executeQuery(String poolName, Connection conn, String sql, Object... params) {

        PreparedStatement ps = null;
        try {
            // 注册事件
            ps = conn.prepareStatement(sql);
        } catch (Exception e) {
            DBManager.freeConnection(poolName, conn);
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
                    DBManager.freeConnection(poolName, conn);
                    LOGGER.error("Error raised when set value in PreparedStatement", e);
                    return null;
                }
            }
        }
        ResultSet res = null;
        try {
            res = ps.executeQuery();
        } catch (SQLException e) {
            DBManager.freeConnection(poolName, conn);
            LOGGER.error("Error raised when querying with sql : " + sql, e);
            return null;
        }
        return res;
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
