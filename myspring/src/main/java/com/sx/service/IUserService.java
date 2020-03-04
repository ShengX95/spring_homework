package com.sx.service;

import com.sx.pojo.User;

import java.sql.SQLException;

/**
 * @author shengx
 * @date 2020/2/28 10:41
 */
public interface IUserService {
    public User getUserById(int id) throws SQLException;
    public void insert(User u)  throws SQLException;
}
