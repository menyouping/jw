package com.jw.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

public class SQLUtils {

    private static final Logger LOGGER = Logger.getLogger(SQLUtils.class);

    private static final Pattern PATTERN_NAMED_PARAMETER = Pattern.compile(":([A-Za-z0-9_]+)");

    public static int update(String dbName, String sql, Map<String, Object> params) {
        DB db = DbManagerFactory.getManager().getDB(dbName);
        return update(db, sql, params);
    }

    public static int update(String dbName, String sql, Object[] params) {
        DB db = DbManagerFactory.getManager().getDB(dbName);
        return update(db, sql, params);
    }

    public static int update(String sql, Map<String, Object> params) {
        DB db = DbManagerFactory.getManager().getDefaultDB();
        return update(db, sql, params);
    }

    public static int update(String sql, Object[] params) {
        DB db = DbManagerFactory.getManager().getDefaultDB();
        return update(db, sql, params);
    }

    public static int update(DB db, String sql, Map<String, Object> params) {
        Connection conn = db.getConnection();
        int result = update(conn, sql, params);
        db.releaseConnection(conn);
        return result;
    }

    public static int update(DB db, String sql, Object[] params) {
        Connection conn = db.getConnection();
        int result = update(conn, sql, params);
        db.releaseConnection(conn);
        return result;
    }

    public static int update(Connection conn, String sql, Map<String, Object> params) {
        List<Object> list = new LinkedList<Object>();
        Matcher m = PATTERN_NAMED_PARAMETER.matcher(sql);
        if (m.find()) {
            StringBuilder sb = new StringBuilder(sql);
            int start = 0, end = 0, offset = 0;
            while (m.find(start)) {
                start = m.start();
                end = m.end();
                sb.replace(start + offset, end + offset, "?");
                list.add(params.get(m.group(1)));
                offset = 1 - (end - start);
                start = end;
            }
            return update(conn, sb.toString(), list.toArray());
        }
        return update(conn, sql, (Object[]) null);
    }

    public static int update(Connection conn, String sql, Object[] params) {
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
                    ps.setObject(i, params[i - 1]);
                } catch (SQLException e) {
                    LOGGER.error("Error raised when set value in PreparedStatement", e);
                    return 0;
                }
            }
        }

        log(ps);

        int i = 0;
        try {
            i = ps.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("Error raised when execute sql : " + sql, e);
        }
        return i;
    }

    public static ResultSet query(String dbName, String sql, Map<String, Object> params) {
        DB db = DbManagerFactory.getManager().getDB(dbName);
        return query(db, sql, params);
    }

    public static ResultSet query(String dbName, String sql, Object[] params) {
        DB db = DbManagerFactory.getManager().getDB(dbName);
        return query(db, sql, params);
    }

    public static ResultSet query(String sql, Map<String, Object> params) {
        DB db = DbManagerFactory.getManager().getDefaultDB();
        return query(db, sql, params);
    }

    public static ResultSet query(String sql, Object[] params) {
        DB db = DbManagerFactory.getManager().getDefaultDB();
        return query(db, sql, params);
    }

    public static ResultSet query(DB db, String sql, Map<String, Object> params) {
        Connection conn = db.getConnection();
        ResultSet rs = query(conn, sql, params);
        db.releaseConnection(conn);
        return rs;
    }

    public static ResultSet query(DB db, String sql, Object[] params) {
        Connection conn = db.getConnection();
        ResultSet rs = query(conn, sql, params);
        db.releaseConnection(conn);
        return rs;
    }

    public static ResultSet query(Connection conn, String sql, Map<String, Object> params) {
        List<Object> list = new LinkedList<Object>();
        Matcher m = PATTERN_NAMED_PARAMETER.matcher(sql);
        if (m.find()) {
            StringBuilder sb = new StringBuilder(sql);
            int start = 0, end = 0, offset = 0;
            while (m.find(start)) {
                start = m.start();
                end = m.end();
                sb.replace(start + offset, end + offset, "?");
                list.add(params.get(m.group(1)));
                offset = 1 - (end - start);
                start = end;
            }
            return query(conn, sb.toString(), list.toArray());
        }
        return query(conn, sql, (Object[]) null);
    }

    public static ResultSet query(Connection conn, String sql, Object[] params) {
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
                    ps.setObject(i, params[i - 1]);
                } catch (SQLException e) {
                    LOGGER.error("Error raised when set value in PreparedStatement", e);
                    return null;
                }
            }
        }

        log(ps);
        ResultSet rs = null;
        try {
            rs = ps.executeQuery();
        } catch (SQLException e) {
            LOGGER.error("Error raised when querying with sql : " + sql, e);
            return null;
        }
        return rs;
    }

    private static void log(PreparedStatement ps) {
        String msg = ps.toString();
        int index = msg.indexOf(":");
        if (index > -1 && index != msg.length() - 1) {
            msg = msg.substring(msg.indexOf(":") + 1);
        }
        LOGGER.info("The sql is " + msg);
    }

    public static void release(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                LOGGER.error("Error raised when close rs.");
                return;
            }
        }
    }
}
