package com.fit_sx.Dao;

import com.fit_sx.model.Admin;
import com.fit_sx.model.User;
import com.fit_sx.util.JsonResult;
import  com.fit_sx.util.SQLUtil;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.fit_sx.util.MD5Util.GetMD5Code;
import static com.fit_sx.util.SQLUtil.findById;
import static com.fit_sx.util.SQLUtil.insert;

public class AdminDaoImpl implements AdminDao{

    JsonResult jsonResult;
    @Override
    public JsonResult Login(Admin admin) {
        jsonResult=new JsonResult();
        admin.setPassword(GetMD5Code(admin.getPassword())+"12345");
        admin.setPassword(GetMD5Code(admin.getPassword()));
        Admin admin1 = findById(Admin.class,"admin",admin.getAccount(),"account");

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

    @Override
    public JsonResult Add(Admin admin) {
        jsonResult=new JsonResult();

        admin.setPassword(GetMD5Code(admin.getPassword())+"12345");
        admin.setPassword(GetMD5Code(admin.getPassword()));
        Admin admin1 = findById(Admin.class,"admin",admin.getAccount(),"account");
        if (admin1!=null){
            jsonResult.setMsg("账号已存在！");
            jsonResult.setCode(2);
        }else {
            Boolean result = insert("admin",admin);
            if (result){
                jsonResult.setCode(1);
                jsonResult.setMsg("注册成功！");
                jsonResult.setData(AdminPage(10,1).getData());
            }else {
                jsonResult.setCode(2);
                jsonResult.setMsg("注册失败！");

            }
        }
        return jsonResult;

    }

    @Override
    public JsonResult AdminPage(Integer pageSize, Integer pageNum) {
        jsonResult = new JsonResult();
        Map<String,Object> map =new HashMap<>();;
        List<User> list =  SQLUtil.find(User.class,"SELECT * FROM admin  limit ?, ?;",(pageNum-1)*pageSize,pageSize);

        BigDecimal count =  SQLUtil.getQueryDecimal("select count(*)  from admin","count(*)");
//        System.out.println(count);
        map.put("rows",list);
        map.put("count",count);
        jsonResult.setData(map);
        return jsonResult;
    }
}
