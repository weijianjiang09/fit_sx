package com.fit_sx.service;

import com.fit_sx.Dao.UserDao;
import com.fit_sx.Dao.UserDaoImp;

import java.util.Map;

public class UserServiceImp implements UserService{
    private UserDao userDao;
    @Override
    public Map UserPage(Integer pageSize, Integer pageNum) {
        userDao = new UserDaoImp();
        return userDao.UserPage(pageSize,pageNum);
    }
}
