package com.fit_sx.service;

import com.fit_sx.Dao.UserDao;
import com.fit_sx.Dao.UserDaoImp;
import com.fit_sx.model.User;

import java.util.Map;

public class UserServiceImp implements UserService{
    private UserDao userDao;
    @Override
    public Map UserPage(Map<String,Object> map) {
        userDao = new UserDaoImp();
        return userDao.UserPage(map);
    }

    @Override
    public Boolean UserCreated(User user) {
        userDao = new UserDaoImp();
        return userDao.UserCrated(user);
    }

    @Override
    public User UserLogin(User user) {
        userDao = new UserDaoImp();
        return userDao.UserLogin(user);
    }
}
