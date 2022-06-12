package com.fit_sx.service;

import com.fit_sx.Dao.AdminDao;
import com.fit_sx.Dao.AdminDaoImpl;
import com.fit_sx.model.Admin;
import com.fit_sx.util.JsonResult;

public class AdminServiceImpl implements AdminService{
    private AdminDao adminDao;

    @Override
    public JsonResult Login(Admin admin) {
        adminDao = new AdminDaoImpl();
        return adminDao.Login(admin);
    }

    @Override
    public JsonResult Add(Admin admin) {
        adminDao = new AdminDaoImpl();
        return adminDao.Add(admin);
    }

    @Override
    public JsonResult AdminPage(Integer pageSize, Integer pageNum) {
        adminDao = new AdminDaoImpl();
        return adminDao.AdminPage(pageSize,pageNum);
    }
}
