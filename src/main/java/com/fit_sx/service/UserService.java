package com.fit_sx.service;

import com.fit_sx.model.User;

import java.util.Map;

public interface UserService {
    public Map UserPage(Map<String,Object> map);
    public Boolean UserCreated(User user);
    public User UserLogin(User user);
}
