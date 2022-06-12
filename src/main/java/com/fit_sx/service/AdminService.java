package com.fit_sx.service;

import com.fit_sx.model.Admin;
import com.fit_sx.util.JsonResult;

public interface AdminService {

    public JsonResult Login(Admin admin);

    public JsonResult Add(Admin admin);

    public JsonResult AdminPage(Integer pageSize, Integer pageNum);
}
