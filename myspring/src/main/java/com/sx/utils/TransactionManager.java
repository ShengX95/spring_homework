package com.sx.utils;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author shengx
 * @date 2020/3/2 18:02
 */
public class TransactionManager {
    private  TransactionManager(){}
    // 类加载的时候会加锁，保证线程安全
    private static class TransactionManagerHolder{
        private static final TransactionManager INSTANCE = new TransactionManager();
    }
    public static final TransactionManager getInstance(){
        return TransactionManagerHolder.INSTANCE;
    }

    public void beginTransaction() throws SQLException {
        Connection conn = ConnectionUtils.getInstance().getConnction();
        conn.setAutoCommit(false);
    }

    public void commit() throws SQLException {
        Connection conn = ConnectionUtils.getInstance().getConnction();
        conn.commit();
    }

    public void rollback() throws SQLException {
        Connection conn = ConnectionUtils.getInstance().getConnction();
        conn.rollback();
    }

    public void close()  throws SQLException {
        Connection conn = ConnectionUtils.getInstance().getConnction();
        conn.close();
    }
}
