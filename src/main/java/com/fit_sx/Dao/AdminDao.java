package com.fit_sx.Dao;

import com.fit_sx.model.Admin;
import com.fit_sx.util.JsonResult;

public interface AdminDao {
    //登录
    public JsonResult Login(Admin admin);
    //注册
    public JsonResult Add(Admin admin);
    //列表
    public JsonResult AdminPage(Integer pageSize, Integer pageNum);
}
