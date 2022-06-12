package com.fit_sx.service;

import com.fit_sx.Dao.AdminDao;
import com.fit_sx.model.Admin;
import com.fit_sx.util.JsonResult;

public class AdminServiceImpl implements AdminService{
    private AdminDao adminDao;

    @Override
    public JsonResult Login(Admin admin) {
        return adminDao.Login(admin);
    }
}
