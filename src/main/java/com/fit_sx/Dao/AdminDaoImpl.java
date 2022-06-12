package com.fit_sx.Dao;

import com.fit_sx.model.Admin;
import com.fit_sx.util.JsonResult;
import com.fit_sx.util.SQLUtil;

import static com.fit_sx.util.SQLUtil.findById;

public class AdminDaoImpl implements AdminDao{

    JsonResult jsonResult;
    @Override
    public JsonResult Login(Admin admin) {
        jsonResult=new JsonResult();
        Admin admin1 = findById(Admin.class,"admin",admin.getAccount());
        if(admin1==null){
            jsonResult.setCode(2);
            jsonResult.setMsg("账号不存在！");
            return jsonResult;

        }else if (!admin1.getPassword().equals(admin.getPassword())){
            jsonResult.setCode(2);
            jsonResult.setMsg("密码错误！");
            return jsonResult;
        }else {
            jsonResult.setCode(1);
            jsonResult.setMsg("登录成功");
            return jsonResult;
        }
    }
}
