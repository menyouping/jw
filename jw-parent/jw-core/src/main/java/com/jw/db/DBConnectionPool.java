package com.jw.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;

import com.jw.util.JwUtils;

/**
 * 此内部类定义了一个连接池. 它能够获取数据库连接,直到预定的最 大连接数为止 在返回连接给客户程序之前,它能够验证连接的有效性
 * 
 * @author shijin
 */
public class DBConnectionPool {

    private static final Logger LOGGER = Logger.getLogger(DBConnectionPool.class);

    private int activeNum = 0;
    private int maxConn = 20;
    private String url = null;
    private String poolName = null;
    private String user = null;
    private String pwd = null;
    private List<Connection> connections = null;

    public DBConnectionPool(String poolName, String url, String user, String pwd, int maxConn) {
        super();
        this.poolName = poolName;
        this.url = url;
        this.user = user;
        this.pwd = pwd;
        this.maxConn = maxConn;
        this.connections = JwUtils.newArrayList(maxConn);
    }

    @SuppressWarnings("resource")
    public synchronized Connection getConnection() {
        Connection conn = null;
        if (!connections.isEmpty()) {
            conn = connections.remove(0);
            try {
                if (conn.isClosed()) {
                    LOGGER.info("The connection peeked from " + poolName + " is closed, try again.");
                    conn = getConnection();
                }
            } catch (SQLException e) {
                LOGGER.info("Error raised when peeking connection from " + poolName, e);
                conn = getConnection();
            }
        } else if (activeNum < maxConn) {
            conn = newConnection();
        } else {
            // 未获得连接
        }
        if (conn != null) {
            activeNum++;
        }
        return conn;
    }

    /**
     * 当无空闲连接而又未达到最大连接数限制时创建新的连接
     * 
     * @return 新创建的连接
     */
    private Connection newConnection() {
        Connection conn = null;
        try {
            if (user == null) {
                conn = DriverManager.getConnection(url);
            } else {
                conn = DriverManager.getConnection(url, user, pwd);
            }
            LOGGER.info("Create a new database connection in " + poolName);
        } catch (SQLException e) {
            LOGGER.error("Failed to create database connection from \"" + url + "\"", e);
        }
        return conn;
    }

    /**
     * 获得一个可用连接，超过最大连接数时线程等待，直到有有连接释放时返回一个可用连接或者超时返回null
     * 
     * @param timeout 等待连接的超时时间，单位为秒
     * @return
     */
    public synchronized Connection getConnection(int timeout) {
        Connection conn = null;
        long startTime = System.currentTimeMillis();
        while ((conn = getConnection()) == null) {
            try {
                // 被notify(),notifyALL()唤醒或者超时自动苏醒
                wait(timeout);
            } catch (InterruptedException e) {
                LOGGER.error("Waiting to get a database connection is interrupted.");
            }
            // 若线程在超时前被唤醒，则不会返回null，继续循环尝试获取连接
            if (System.currentTimeMillis() - startTime > timeout * 1000000)
                return null;
        }
        return conn;
    }

    /**
     * 将释放的空闲连接加入空闲连接池，活跃连接数减一并激活等待连接的线程
     * 
     * @param conn
     *            释放的连接
     */
    public synchronized void freeConnection(Connection conn) {
        connections.add(conn);
        activeNum--;
        notifyAll();// 通知正在由于达到最大连接数限制而wait的线程获取连接
    }

    /**
     * 关闭空闲连接池中的所有连接
     */
    public synchronized void releaseAll() {
        for (Connection conn : connections) {
            try {
                conn.close();
                LOGGER.info("Close the idle database connection in " + poolName);
            } catch (SQLException e) {
                LOGGER.info("Error raised when closing the idle database connection in " + poolName, e);
            }
        }
        connections.clear();
    }
}
