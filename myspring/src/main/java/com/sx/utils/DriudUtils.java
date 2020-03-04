package com.sx.utils;

import com.alibaba.druid.pool.DruidDataSource;

/**
 * @author shengx
 * @date 2020/2/28 10:48
 */
public class DriudUtils {
    private static volatile DruidDataSource dataSource;
    private DriudUtils(){}
    public static DruidDataSource getInstance(){
        if (dataSource == null){
            synchronized (DriudUtils.class){
                if(dataSource == null){
                    dataSource = new DruidDataSource();
                    dataSource.setDriverClassName("com.mysql.jdbc.Driver");
                    dataSource.setUrl("jdbc:mysql://106.12.76.148:3306/test?characterEncoding=utf8");
                    dataSource.setUsername("root");
                    dataSource.setPassword("wjdh84928399");
                }
            }
        }
        return dataSource;
    }
}
