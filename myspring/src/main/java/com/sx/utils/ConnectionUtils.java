package com.sx.utils;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author shengx
 * @date 2020/3/1 21:15
 */
public class ConnectionUtils {
    private static volatile ConnectionUtils connectionUtils;
    private ConnectionUtils(){}
    private ThreadLocal<Connection> connectionThreadLocal = new ThreadLocal<>();

    public Connection getConnction() throws SQLException {
        Connection connection = connectionThreadLocal.get();
        if(connection == null){
            connection = DriudUtils.getInstance().getConnection();
            connectionThreadLocal.set(connection);
        }
        return connection;
    }

    public static ConnectionUtils getInstance(){
        if(connectionUtils == null){
            synchronized (ConnectionUtils.class){
                if(connectionUtils == null){
                    connectionUtils = new ConnectionUtils();
                }
            }
        }
        return connectionUtils;
    }

}
