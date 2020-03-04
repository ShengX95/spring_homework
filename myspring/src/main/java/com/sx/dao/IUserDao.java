package com.sx.dao;

import com.sx.pojo.User;

import java.sql.SQLException;

/**
 * @author shengx
 * @date 2020/2/28 10:27
 */
public interface IUserDao {

    public User findById(int id) throws SQLException;

    public void insert(User u) throws SQLException;

    public void deleteById(int id);

    public void updateById(User u);
}
