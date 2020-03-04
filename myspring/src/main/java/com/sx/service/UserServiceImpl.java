package com.sx.service;

import com.sx.anno.Service;
import com.sx.pojo.User;
import com.sx.dao.IUserDao;
import com.sx.dao.UserDaoImpl;

import java.sql.SQLException;

/**
 * @author shengx
 * @date 2020/2/28 10:42
 */
public class UserServiceImpl implements IUserService{
    private IUserDao userDao;

    public void setUserDao(IUserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public User getUserById(int id) throws SQLException {
        return userDao.findById(id);
    }

    @Override
    public void insert(User u) throws SQLException {
        userDao.insert(u);
    }
}
