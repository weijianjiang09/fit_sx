package com.fit_sx.Dao;

import com.fit_sx.model.Admin;
import com.fit_sx.util.JsonResult;

public interface AdminDao {
    public JsonResult Login(Admin admin);
}
