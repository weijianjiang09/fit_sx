package com.fit_sx.Dao;

import com.fit_sx.model.User;

import java.util.Map;

public interface UserDao {
    public Map UserPage(Map<String,Object> map);
    public boolean UserCrated(User user);
    public User UserLogin(User user);
}
