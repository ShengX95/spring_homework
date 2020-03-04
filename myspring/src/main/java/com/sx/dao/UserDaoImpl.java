package com.sx.dao;

import com.alibaba.druid.pool.DruidDataSource;
import com.sx.anno.Repository;
import com.sx.utils.DriudUtils;
import com.sx.pojo.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author shengx
 * @date 2020/2/28 10:28
 */
public class UserDaoImpl implements IUserDao {
    @Override
    public User findById(int id) throws SQLException {
        DruidDataSource dataSource = DriudUtils.getInstance();
        Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement("select * from user where id=?");
        statement.setInt(1, id);
        ResultSet resultSet = statement.executeQuery();
        User u = new User();
        while(resultSet.next()){
            int mid = resultSet.getInt(1);
            String name = resultSet.getString(2);
            int age = resultSet.getInt(3);
            String email = resultSet.getString(4);
            u.setId(mid);
            u.setName(name);
            u.setAge(age);
            u.setEmail(email);
        }
        statement.close();
        connection.close();
        return u;
    }

    @Override
    public void insert(User u) throws SQLException {
        DruidDataSource dataSource = DriudUtils.getInstance();
        Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement("insert into user values (?,?,?,?)");
        statement.setInt(1, u.getId());
        statement.setString(2, u.getName());
        statement.setInt(3, u.getAge());
        statement.setString(4, u.getEmail());
        statement.execute();
        statement.close();
        connection.close();
    }

    @Override
    public void deleteById(int id) {

    }

    @Override
    public void updateById(User u) {

    }
}
